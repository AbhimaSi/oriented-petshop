const initialServices = [
    {
        id: 1,
        nome: "Banho",
        descricao: "Higienizacao completa do animal",
        duracao: "45 min",
        preco: "R$ 55,00",
        status: "Ativo"
    },
    {
        id: 2,
        nome: "Tosa",
        descricao: "Tosa conforme necessidade do animal",
        duracao: "60 min",
        preco: "R$ 75,00",
        status: "Ativo"
    },
    {
        id: 3,
        nome: "Consulta veterinaria",
        descricao: "Avaliacao clinica com veterinario",
        duracao: "30 min",
        preco: "R$ 120,00",
        status: "Ativo"
    }
];

const servicesTableBody = document.getElementById("servicesTableBody");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const serviceForm = document.getElementById("serviceForm");
const modalTitle = document.getElementById("modalTitle");

let services = [...initialServices];

const servicesService = {
    async list() {
        return [...services];
    },

    async save(service) {
        if (service.id) {
            services = services.map((item) =>
                item.id === service.id ? service : item
            );

            return service;
        }

        const nextId = services.length
            ? Math.max(...services.map((item) => item.id)) + 1
            : 1;

        const createdService = {
            ...service,
            id: nextId
        };

        services.push(createdService);
        return createdService;
    },

    async remove(id) {
        services = services.filter((service) => service.id !== id);
    }
};

function getStatusClass(status) {
    return status === "Inativo" ? "danger" : "";
}

function renderServices() {
    if (!services.length) {
        servicesTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="6">Nenhum servico encontrado.</td>
            </tr>
        `;
        return;
    }

    servicesTableBody.innerHTML = services.map((service) => `
        <tr>
            <td>${service.nome}</td>
            <td>${service.descricao}</td>
            <td>${service.duracao}</td>
            <td>${service.preco}</td>
            <td>
                <span class="status-badge ${getStatusClass(service.status)}">
                    ${service.status}
                </span>
            </td>
            <td>
                <div class="table-actions">
                    <button class="small-button" data-action="edit" data-id="${service.id}" type="button">
                        Editar
                    </button>
                    <button class="small-button" data-action="delete" data-id="${service.id}" type="button">
                        Excluir
                    </button>
                </div>
            </td>
        </tr>
    `).join("");
}

function openModal(service = null) {
    modalTitle.textContent = service ? "Editar servico" : "Novo servico";
    serviceForm.reset();

    document.getElementById("serviceId").value = service ? service.id : "";
    document.getElementById("nome").value = service ? service.nome : "";
    document.getElementById("descricao").value = service ? service.descricao : "";
    document.getElementById("duracao").value = service ? service.duracao : "";
    document.getElementById("preco").value = service ? service.preco : "";
    document.getElementById("status").value = service ? service.status : "Ativo";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    serviceForm.reset();
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(serviceForm);
    const service = Object.fromEntries(formData.entries());

    await servicesService.save({
        ...service,
        id: service.id ? Number(service.id) : null
    });

    closeModal();
    renderServices();
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    const service = services.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(service);
        return;
    }

    await servicesService.remove(id);
    renderServices();
}

openModalButton.addEventListener("click", () => openModal());
closeModalButton.addEventListener("click", closeModal);
cancelModalButton.addEventListener("click", closeModal);
serviceForm.addEventListener("submit", handleSubmit);
servicesTableBody.addEventListener("click", handleTableClick);

modalBackdrop.addEventListener("click", (event) => {
    if (event.target === modalBackdrop) {
        closeModal();
    }
});

servicesService.list().then((records) => {
    services = records;
    renderServices();
});
