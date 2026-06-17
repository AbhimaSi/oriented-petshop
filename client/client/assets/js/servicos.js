const API_BASE_URL = localStorage.getItem("javaPetApiBaseUrl") || "http://localhost:8080";

const servicesTableBody = document.getElementById("servicesTableBody");
const serviceFeedback = document.getElementById("serviceFeedback");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const serviceForm = document.getElementById("serviceForm");
const modalTitle = document.getElementById("modalTitle");

let services = [];

const servicesService = {
    async list() {
        return apiRequest("/servicos/listar");
    },

    async save(service) {
        if (service.id) {
            return apiRequest(`/servicos/${service.id}`, {
                method: "PUT",
                body: JSON.stringify(service)
            });
        }

        return apiRequest("/servicos", {
            method: "POST",
            body: JSON.stringify(service)
        });
    },

    async remove(id) {
        return apiRequest(`/servicos/${id}`, {
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

function normalizeService(service) {
    return {
        id: service.id ? String(service.id) : "",
        nome: service.nome || "",
        descricao: service.descricao || "",
        preco: service.preco ?? ""
    };
}

function buildServicePayload(formData) {
    const service = Object.fromEntries(formData.entries());

    return {
        id: service.id ? Number(service.id) : null,
        nome: service.nome.trim(),
        descricao: service.descricao.trim(),
        preco: service.preco ? Number(service.preco) : 0
    };
}

function formatPrice(value) {
    if (value === "" || value === null || value === undefined) {
        return "-";
    }

    return Number(value).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
}

function setFeedback(message, type = "") {
    serviceFeedback.textContent = message;
    serviceFeedback.dataset.type = type;
}

function renderLoading() {
    servicesTableBody.innerHTML = `
        <tr>
            <td class="empty-row" colspan="4">Carregando servicos...</td>
        </tr>
    `;
}

function renderServices() {
    if (!services.length) {
        servicesTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="4">Nenhum servico encontrado.</td>
            </tr>
        `;
        return;
    }

    servicesTableBody.innerHTML = services.map((service) => `
        <tr>
            <td>${service.nome}</td>
            <td>${service.descricao || "-"}</td>
            <td>${formatPrice(service.preco)}</td>
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
    document.getElementById("preco").value = service ? service.preco : "";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    serviceForm.reset();
}

async function loadServices() {
    renderLoading();
    setFeedback("");

    try {
        const records = await servicesService.list();
        services = Array.isArray(records) ? records.map(normalizeService) : [];
        renderServices();
    } catch (error) {
        services = [];
        renderServices();
        setFeedback(
            "Nao foi possivel carregar os servicos. Verifique se a API possui a rota /servicos/listar.",
            "error"
        );
    }
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(serviceForm);
    const service = buildServicePayload(formData);

    try {
        await servicesService.save(service);
        closeModal();
        await loadServices();
    } catch (error) {
        setFeedback("Nao foi possivel salvar o servico. Confira a API de servicos.", "error");
    }
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = button.dataset.id;
    const service = services.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(service);
        return;
    }

    if (!confirm("Excluir este servico?")) {
        return;
    }

    try {
        await servicesService.remove(id);
        await loadServices();
    } catch (error) {
        setFeedback("Nao foi possivel excluir o servico. Confira a API de servicos.", "error");
    }
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

loadServices();
