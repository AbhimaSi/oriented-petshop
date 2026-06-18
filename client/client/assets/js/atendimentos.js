const API_BASE_URL = localStorage.getItem("javaPetApiBaseUrl") || "http://localhost:8080";

const appointmentsTableBody = document.getElementById("appointmentsTableBody");
const appointmentFeedback = document.getElementById("appointmentFeedback");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const appointmentForm = document.getElementById("appointmentForm");
const modalTitle = document.getElementById("modalTitle");
const clientSelect = document.getElementById("idCliente");
const animalSelect = document.getElementById("idAnimal");
const serviceSelect = document.getElementById("idServico");
const employeeSelect = document.getElementById("idFuncionario");

let appointments = [];
let clients = [];
let animals = [];
let services = [];
let employees = [];

const appointmentsService = {
    async list() {
        return apiRequest("/atendimentos/listar");
    },

    async save(appointment) {
        if (appointment.id) {
            return apiRequest(`/atendimentos/${appointment.id}`, {
                method: "PUT",
                body: JSON.stringify(appointment)
            });
        }

        return apiRequest("/atendimentos", {
            method: "POST",
            body: JSON.stringify(appointment)
        });
    },

    async remove(id) {
        return apiRequest(`/atendimentos/${id}`, {
            method: "DELETE"
        });
    }
};

async function apiRequest(path, options = {}) {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        headers: {
            "Content-Type": "application/json",
            ...options.headers
        },
        ...options
    });

    if (!response.ok) {
        throw new Error(`Erro ${response.status} ao chamar ${path}`);
    }

    if (response.status === 204) {
        return null;
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

function normalizeAppointment(appointment) {
    return {
        id: appointment.id ? String(appointment.id) : "",
        idAnimal: appointment.idAnimal ? String(appointment.idAnimal) : "",
        idServico: appointment.idServico ? String(appointment.idServico) : "",
        idFuncionario: appointment.idFuncionario ? String(appointment.idFuncionario) : "",
        duracao: appointment.duracao ?? 0,
        data: appointment.data || "",
        hora: appointment.hora || "",
        status: appointment.status || "AGENDADO"
    };
}

function buildAppointmentPayload(formData) {
    const appointment = Object.fromEntries(formData.entries());

    return {
        id: appointment.id ? Number(appointment.id) : null,
        data: appointment.data,
        hora: appointment.hora,
        status: appointment.status,
        idAnimal: Number(appointment.idAnimal),
        idServico: Number(appointment.idServico),
        idFuncionario: Number(appointment.idFuncionario),
        duracao: Number(appointment.duracao)
    };
}

function normalizeRecords(records) {
    return Array.isArray(records) ? records.map((record) => ({
        ...record,
        id: record.id ? String(record.id) : "",
        idCliente: record.idCliente ? String(record.idCliente) : ""
    })) : [];
}

function setSelectOptions(select, records, placeholder, selectedId = "") {
    select.innerHTML = `
        <option value="">${placeholder}</option>
        ${records.map((record) => `
            <option value="${record.id}">${record.nome}</option>
        `).join("")}
    `;
    select.value = selectedId;
}

function populateAnimalOptions(clientId, selectedId = "") {
    const filteredAnimals = animals.filter((animal) => animal.idCliente === clientId);
    const placeholder = filteredAnimals.length
        ? "Selecione o animal"
        : "Nenhum animal cadastrado";

    setSelectOptions(animalSelect, filteredAnimals, placeholder, selectedId);
    animalSelect.disabled = !clientId || !filteredAnimals.length;
}

function findById(records, id) {
    return records.find((record) => record.id === String(id));
}

function formatDate(value) {
    if (!value) {
        return "-";
    }

    const [year, month, day] = value.split("-");
    return day && month && year ? `${day}/${month}/${year}` : value;
}

function formatTime(value) {
    return value ? value.slice(0, 5) : "";
}

function normalizeStatus(status) {
    return String(status || "").trim().toUpperCase();
}

function getStatusClass(status) {
    const normalizedStatus = normalizeStatus(status);

    if (normalizedStatus === "CANCELADO") {
        return "danger";
    }

    if (normalizedStatus === "EM ANDAMENTO") {
        return "warning";
    }

    return "";
}

function setFeedback(message, type = "") {
    appointmentFeedback.textContent = message;
    appointmentFeedback.dataset.type = type;
}

function renderLoading() {
    appointmentsTableBody.innerHTML = `
        <tr>
            <td class="empty-row" colspan="9">Carregando atendimentos...</td>
        </tr>
    `;
}

function renderAppointments() {
    if (!appointments.length) {
        appointmentsTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="9">Nenhum atendimento encontrado.</td>
            </tr>
        `;
        return;
    }

    appointmentsTableBody.innerHTML = appointments.map((appointment) => {
        const animal = findById(animals, appointment.idAnimal);
        const client = animal ? findById(clients, animal.idCliente) : null;
        const service = findById(services, appointment.idServico);
        const employee = findById(employees, appointment.idFuncionario);

        return `
            <tr>
                <td>${client ? client.nome : "-"}</td>
                <td>${animal ? animal.nome : "-"}</td>
                <td>${service ? service.nome : "-"}</td>
                <td>${employee ? employee.nome : "-"}</td>
                <td>${appointment.duracao} min</td>
                <td>${formatDate(appointment.data)}</td>
                <td>${formatTime(appointment.hora)}</td>
                <td>
                    <span class="status-badge ${getStatusClass(appointment.status)}">
                        ${appointment.status}
                    </span>
                </td>
                <td>
                    <div class="table-actions">
                        <button class="small-button" data-action="edit" data-id="${appointment.id}" type="button">
                            Editar
                        </button>
                        <button class="small-button" data-action="delete" data-id="${appointment.id}" type="button">
                            Excluir
                        </button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

function openModal(appointment = null) {
    modalTitle.textContent = appointment ? "Editar atendimento" : "Novo atendimento";
    appointmentForm.reset();

    const animal = appointment ? findById(animals, appointment.idAnimal) : null;
    const clientId = animal ? animal.idCliente : "";

    setSelectOptions(clientSelect, clients, "Selecione o cliente", clientId);
    populateAnimalOptions(clientId, appointment ? appointment.idAnimal : "");
    setSelectOptions(serviceSelect, services, "Selecione o servico", appointment ? appointment.idServico : "");
    setSelectOptions(employeeSelect, employees, "Selecione o funcionario", appointment ? appointment.idFuncionario : "");

    document.getElementById("appointmentId").value = appointment ? appointment.id : "";
    document.getElementById("duracao").value = appointment ? appointment.duracao : "";
    document.getElementById("data").value = appointment ? appointment.data : "";
    document.getElementById("hora").value = appointment ? formatTime(appointment.hora) : "";
    document.getElementById("status").value = appointment ? normalizeStatus(appointment.status) : "AGENDADO";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    appointmentForm.reset();
}

async function loadAppointments() {
    const records = await appointmentsService.list();
    appointments = Array.isArray(records) ? records.map(normalizeAppointment) : [];
    renderAppointments();
}

async function loadPageData() {
    renderLoading();
    setFeedback("");

    try {
        const clientRecords = await apiRequest("/clientes/listar");
        const animalRecords = await apiRequest("/animais/listar");
        const serviceRecords = await apiRequest("/servicos/listar");
        const employeeRecords = await apiRequest("/funcionarios/listar");
        const appointmentRecords = await appointmentsService.list();

        clients = normalizeRecords(clientRecords);
        animals = normalizeRecords(animalRecords);
        services = normalizeRecords(serviceRecords);
        employees = normalizeRecords(employeeRecords);
        appointments = Array.isArray(appointmentRecords)
            ? appointmentRecords.map(normalizeAppointment)
            : [];
        renderAppointments();
    } catch (error) {
        appointments = [];
        renderAppointments();
        setFeedback("Nao foi possivel carregar os dados do atendimento.", "error");
    }
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(appointmentForm);
    const appointment = buildAppointmentPayload(formData);

    try {
        await appointmentsService.save(appointment);
        closeModal();
        await loadAppointments();
    } catch (error) {
        setFeedback("Nao foi possivel salvar o atendimento. Confira a API de atendimentos.", "error");
    }
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = button.dataset.id;
    const appointment = appointments.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(appointment);
        return;
    }

    if (!confirm("Excluir este atendimento?")) {
        return;
    }

    try {
        await appointmentsService.remove(id);
        await loadAppointments();
    } catch (error) {
        setFeedback("Nao foi possivel excluir o atendimento. Confira a API de atendimentos.", "error");
    }
}

clientSelect.addEventListener("change", () => {
    populateAnimalOptions(clientSelect.value);
});
openModalButton.addEventListener("click", () => openModal());
closeModalButton.addEventListener("click", closeModal);
cancelModalButton.addEventListener("click", closeModal);
appointmentForm.addEventListener("submit", handleSubmit);
appointmentsTableBody.addEventListener("click", handleTableClick);

modalBackdrop.addEventListener("click", (event) => {
    if (event.target === modalBackdrop) {
        closeModal();
    }
});

loadPageData();
