const initialAppointments = [
    {
        id: 1,
        cliente: "Mariana Costa",
        animal: "Mel",
        servico: "Banho e tosa",
        funcionario: "Carlos",
        data: "2026-06-18",
        horario: "09:00",
        status: "Agendado"
    },
    {
        id: 2,
        cliente: "Lucas Ferreira",
        animal: "Tom",
        servico: "Consulta",
        funcionario: "Fernanda",
        data: "2026-06-18",
        horario: "14:30",
        status: "Em andamento"
    },
    {
        id: 3,
        cliente: "Ana Souza",
        animal: "Luna",
        servico: "Vacina",
        funcionario: "Carlos",
        data: "2026-06-19",
        horario: "10:00",
        status: "Concluido"
    }
];

const appointmentsTableBody = document.getElementById("appointmentsTableBody");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const appointmentForm = document.getElementById("appointmentForm");
const modalTitle = document.getElementById("modalTitle");

let appointments = [...initialAppointments];
const appointmentsService = {
    async list() {
        return [...appointments];
    },

    async save(appointment) {
        if (appointment.id) {
            appointments = appointments.map((item) =>
                item.id === appointment.id ? appointment : item
            );

            return appointment;
        }

        const nextId = appointments.length
            ? Math.max(...appointments.map((item) => item.id)) + 1
            : 1;

        const createdAppointment = {
            ...appointment,
            id: nextId
        };

        appointments.push(createdAppointment);
        return createdAppointment;
    },

    async remove(id) {
        appointments = appointments.filter((appointment) => appointment.id !== id);
    }
};

function formatDate(value) {
    const [year, month, day] = value.split("-");
    return `${day}/${month}/${year}`;
}

function getStatusClass(status) {
    if (status === "Cancelado") {
        return "danger";
    }

    if (status === "Em andamento") {
        return "warning";
    }

    return "";
}

function renderAppointments() {
    const filteredAppointments = appointments;

    if (!filteredAppointments.length) {
        appointmentsTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="7">Nenhum atendimento encontrado.</td>
            </tr>
        `;
        return;
    }

    appointmentsTableBody.innerHTML = filteredAppointments.map((appointment) => `
        <tr>
            <td>${appointment.cliente}</td>
            <td>${appointment.animal}</td>
            <td>${appointment.servico}</td>
            <td>${appointment.funcionario}</td>
            <td>${formatDate(appointment.data)} ${appointment.horario}</td>
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
    document.getElementById("cliente").value = appointment ? appointment.cliente : "";
    document.getElementById("animal").value = appointment ? appointment.animal : "";
    document.getElementById("servico").value = appointment ? appointment.servico : "";
    document.getElementById("funcionario").value = appointment ? appointment.funcionario : "";
    document.getElementById("data").value = appointment ? appointment.data : "";
    document.getElementById("horario").value = appointment ? appointment.horario : "";
    document.getElementById("status").value = appointment ? appointment.status : "Agendado";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    appointmentForm.reset();
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(appointmentForm);
    const appointment = Object.fromEntries(formData.entries());

    await appointmentsService.save({
        ...appointment,
        id: appointment.id ? Number(appointment.id) : null
    });

    closeModal();
    renderAppointments();
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    const appointment = appointments.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(appointment);
        return;
    }

    await appointmentsService.remove(id);
    renderAppointments();
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

appointmentsService.list().then((records) => {
    appointments = records;
    renderAppointments();
});
