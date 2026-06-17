const API_BASE_URL = localStorage.getItem("javaPetApiBaseUrl") || "http://localhost:8080";

const clientsTableBody = document.getElementById("clientsTableBody");
const clientFeedback = document.getElementById("clientFeedback");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const clientForm = document.getElementById("clientForm");
const modalTitle = document.getElementById("modalTitle");

let clients = [];

const clientsService = {
    async list() {
        return apiRequest("/clientes/listar");
    },

    async save(client) {
        if (client.id) {
            return apiRequest(`/clientes/${client.id}`, {
                method: "PUT",
                body: JSON.stringify(client)
            });
        }

        return apiRequest("/clientes", {
            method: "POST",
            body: JSON.stringify(client)
        });
    },

    async remove(id) {
        return apiRequest(`/clientes/${id}`, {
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

function normalizeClient(client) {
    return {
        id: client.id ? String(client.id) : "",
        nome: client.nome || "",
        telefone: client.telefone || "",
        endereco: client.endereco || ""
    };
}

function buildClientPayload(formData) {
    const client = Object.fromEntries(formData.entries());

    return {
        id: client.id ? Number(client.id) : null,
        nome: client.nome.trim(),
        telefone: client.telefone.trim(),
        endereco: client.endereco.trim()
    };
}

function setFeedback(message, type = "") {
    clientFeedback.textContent = message;
    clientFeedback.dataset.type = type;
}

function renderLoading() {
    clientsTableBody.innerHTML = `
        <tr>
            <td class="empty-row" colspan="4">Carregando clientes...</td>
        </tr>
    `;
}

function renderClients() {
    if (!clients.length) {
        clientsTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="4">Nenhum cliente encontrado.</td>
            </tr>
        `;
        return;
    }

    clientsTableBody.innerHTML = clients.map((client) => `
        <tr>
            <td>${client.nome}</td>
            <td>${client.telefone}</td>
            <td>${client.endereco}</td>
            <td>
                <div class="table-actions">
                    <button class="small-button" data-action="edit" data-id="${client.id}" type="button">
                        Editar
                    </button>
                    <button class="small-button" data-action="delete" data-id="${client.id}" type="button">
                        Excluir
                    </button>
                </div>
            </td>
        </tr>
    `).join("");
}

function openModal(client = null) {
    modalTitle.textContent = client ? "Editar cliente" : "Novo cliente";
    clientForm.reset();

    document.getElementById("clientId").value = client ? client.id : "";
    document.getElementById("nome").value = client ? client.nome : "";
    document.getElementById("telefone").value = client ? client.telefone : "";
    document.getElementById("endereco").value = client ? client.endereco : "";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    clientForm.reset();
}

async function loadClients() {
    renderLoading();
    setFeedback("");

    try {
        const records = await clientsService.list();
        clients = Array.isArray(records) ? records.map(normalizeClient) : [];
        renderClients();
    } catch (error) {
        clients = [];
        renderClients();
        setFeedback(
            "Nao foi possivel carregar os clientes. Verifique se a API possui a rota /clientes/listar.",
            "error"
        );
    }
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(clientForm);
    const client = buildClientPayload(formData);

    try {
        await clientsService.save(client);
        closeModal();
        await loadClients();
    } catch (error) {
        setFeedback("Nao foi possivel salvar o cliente. Confira a API de clientes.", "error");
    }
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = button.dataset.id;
    const client = clients.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(client);
        return;
    }

    if (!confirm("Excluir este cliente?")) {
        return;
    }

    try {
        await clientsService.remove(id);
        await loadClients();
    } catch (error) {
        setFeedback("Nao foi possivel excluir o cliente. Confira a API de clientes.", "error");
    }
}

openModalButton.addEventListener("click", () => openModal());
closeModalButton.addEventListener("click", closeModal);
cancelModalButton.addEventListener("click", closeModal);
clientForm.addEventListener("submit", handleSubmit);
clientsTableBody.addEventListener("click", handleTableClick);

modalBackdrop.addEventListener("click", (event) => {
    if (event.target === modalBackdrop) {
        closeModal();
    }
});

loadClients();
