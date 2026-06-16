const API_BASE_URL = localStorage.getItem("javaPetApiBaseUrl") || "http://localhost:8080";

const appointmentsTableBody = document.getElementById("appointmentsTableBody");
const appointmentFeedback = document.getElementById("appointmentFeedback");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const appointmentForm = document.getElementById("appointmentForm");
const modalTitle = document.getElementById("modalTitle");

let appointments = [];

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
    const services = Array.isArray(appointment.servicos) ? appointment.servicos : [];
    const firstService = services[0] || {};

    return {
        id: appointment.id ? String(appointment.id) : "",
        idPet: appointment.idPet || appointment.idpet || appointment.idAnimal || appointment.idanimal || "",
        idFuncionario: appointment.idFuncionario || appointment.idfuncionario || "",
        idServico: firstService.id ? String(firstService.id) : appointment.idServico || appointment.idservico || "",
        data: appointment.data || appointment.dataAtendimento || appointment.data_atendimento || "",
        hora: appointment.hora || appointment.horaAtendimento || appointment.hora_atendimento || "",
        status: appointment.status || "AGENDADO",
        servicos: services
    };
}

function buildAppointmentPayload(formData) {
    const appointment = Object.fromEntries(formData.entries());

    return {
        id: appointment.id || null,
        data: appointment.data,
        hora: appointment.hora,
        status: appointment.status,
        idPet: appointment.idPet,
        idFuncionario: appointment.idFuncionario ? Number(appointment.idFuncionario) : null,
        servicos: appointment.idServico
            ? [{ id: Number(appointment.idServico) }]
            : []
    };
}

function formatDate(value) {
    if (!value) {
        return "-";
    }

    const [year, month, day] = value.split("-");
    return day && month && year ? `${day}/${month}/${year}` : value;
}

function formatTime(value) {
    if (!value) {
        return "";
    }

    return value.slice(0, 5);
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

function normalizeStatus(status) {
    return String(status || "").trim().toUpperCase();
}

function setFeedback(message, type = "") {
    appointmentFeedback.textContent = message;
    appointmentFeedback.dataset.type = type;
}

function renderLoading() {
    appointmentsTableBody.innerHTML = `
        <tr>
            <td class="empty-row" colspan="7">Carregando atendimentos...</td>
        </tr>
    `;
}

function renderAppointments() {
    if (!appointments.length) {
        appointmentsTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="7">Nenhum atendimento encontrado.</td>
            </tr>
        `;
        return;
    }

    appointmentsTableBody.innerHTML = appointments.map((appointment) => `
        <tr>
            <td>${appointment.idPet || "-"}</td>
            <td>${appointment.idServico || "-"}</td>
            <td>${appointment.idFuncionario || "-"}</td>
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
    `).join("");
}

function openModal(appointment = null) {
    modalTitle.textContent = appointment ? "Editar atendimento" : "Novo atendimento";
    appointmentForm.reset();

    document.getElementById("appointmentId").value = appointment ? appointment.id : "";
    document.getElementById("idPet").value = appointment ? appointment.idPet : "";
    document.getElementById("idServico").value = appointment ? appointment.idServico : "";
    document.getElementById("idFuncionario").value = appointment ? appointment.idFuncionario : "";
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
    renderLoading();
    setFeedback("");

    try {
        const records = await appointmentsService.list();
        appointments = Array.isArray(records) ? records.map(normalizeAppointment) : [];
        renderAppointments();
    } catch (error) {
        appointments = [];
        renderAppointments();
        setFeedback(
            "Nao foi possivel carregar os atendimentos. Verifique se a API possui a rota /atendimentos/listar.",
            "error"
        );
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

loadAppointments();
