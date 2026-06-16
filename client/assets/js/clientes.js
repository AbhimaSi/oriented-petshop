const initialClients = [
    {
        id: 1,
        nome: "Mariana Costa",
        cpf: "123.456.789-00",
        telefone: "(11) 98888-1234",
        email: "mariana@email.com",
        endereco: "Rua das Flores, 120"
    },
    {
        id: 2,
        nome: "Lucas Ferreira",
        cpf: "987.654.321-00",
        telefone: "(11) 97777-9876",
        email: "lucas@email.com",
        endereco: "Avenida Brasil, 450"
    },
    {
        id: 3,
        nome: "Ana Souza",
        cpf: "456.789.123-00",
        telefone: "(11) 96666-4321",
        email: "ana@email.com",
        endereco: "Rua do Parque, 78"
    }
];

const clientsTableBody = document.getElementById("clientsTableBody");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const clientForm = document.getElementById("clientForm");
const modalTitle = document.getElementById("modalTitle");

let clients = [...initialClients];

const clientsService = {
    async list() {
        return [...clients];
    },

    async save(client) {
        if (client.id) {
            clients = clients.map((item) =>
                item.id === client.id ? client : item
            );

            return client;
        }

        const nextId = clients.length
            ? Math.max(...clients.map((item) => item.id)) + 1
            : 1;

        const createdClient = {
            ...client,
            id: nextId
        };

        clients.push(createdClient);
        return createdClient;
    },

    async remove(id) {
        clients = clients.filter((client) => client.id !== id);
    }
};

function renderClients() {
    if (!clients.length) {
        clientsTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="6">Nenhum cliente encontrado.</td>
            </tr>
        `;
        return;
    }

    clientsTableBody.innerHTML = clients.map((client) => `
        <tr>
            <td>${client.nome}</td>
            <td>${client.cpf}</td>
            <td>${client.telefone}</td>
            <td>${client.email}</td>
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
    document.getElementById("cpf").value = client ? client.cpf : "";
    document.getElementById("telefone").value = client ? client.telefone : "";
    document.getElementById("email").value = client ? client.email : "";
    document.getElementById("endereco").value = client ? client.endereco : "";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    clientForm.reset();
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(clientForm);
    const client = Object.fromEntries(formData.entries());

    await clientsService.save({
        ...client,
        id: client.id ? Number(client.id) : null
    });

    closeModal();
    renderClients();
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    const client = clients.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(client);
        return;
    }

    await clientsService.remove(id);
    renderClients();
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

clientsService.list().then((records) => {
    clients = records;
    renderClients();
});
