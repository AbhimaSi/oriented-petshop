const API_BASE_URL = localStorage.getItem("javaPetApiBaseUrl") || "http://localhost:8080";

const animalsTableBody = document.getElementById("animalsTableBody");
const animalFeedback = document.getElementById("animalFeedback");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const animalForm = document.getElementById("animalForm");
const modalTitle = document.getElementById("modalTitle");

let animals = [];

const animalsService = {
    async list() {
        return apiRequest("/animais/listar");
    },

    async save(animal) {
        if (animal.id) {
            return apiRequest(`/animais/${animal.id}`, {
                method: "PUT",
                body: JSON.stringify(animal)
            });
        }

        return apiRequest("/animais", {
            method: "POST",
            body: JSON.stringify(animal)
        });
    },

    async remove(id) {
        return apiRequest(`/animais/${id}`, {
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

function normalizeAnimal(animal) {
    return {
        id: animal.id ? String(animal.id) : "",
        nome: animal.nome || "",
        especie: animal.especie || "",
        raca: animal.raca || "",
        idCliente: animal.idCliente || animal.idcliente || ""
    };
}

function buildAnimalPayload(formData) {
    const animal = Object.fromEntries(formData.entries());

    return {
        id: animal.id ? Number(animal.id) : null,
        nome: animal.nome.trim(),
        especie: animal.especie,
        raca: animal.raca.trim(),
        idCliente: animal.idCliente ? Number(animal.idCliente) : 0
    };
}

function setFeedback(message, type = "") {
    animalFeedback.textContent = message;
    animalFeedback.dataset.type = type;
}

function renderLoading() {
    animalsTableBody.innerHTML = `
        <tr>
            <td class="empty-row" colspan="5">Carregando animais...</td>
        </tr>
    `;
}

function renderAnimals() {
    if (!animals.length) {
        animalsTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="5">Nenhum animal encontrado.</td>
            </tr>
        `;
        return;
    }

    animalsTableBody.innerHTML = animals.map((animal) => `
        <tr>
            <td>${animal.nome}</td>
            <td>${animal.idCliente || "-"}</td>
            <td>${animal.especie}</td>
            <td>${animal.raca}</td>
            <td>
                <div class="table-actions">
                    <button class="small-button" data-action="edit" data-id="${animal.id}" type="button">
                        Editar
                    </button>
                    <button class="small-button" data-action="delete" data-id="${animal.id}" type="button">
                        Excluir
                    </button>
                </div>
            </td>
        </tr>
    `).join("");
}

function openModal(animal = null) {
    modalTitle.textContent = animal ? "Editar animal" : "Novo animal";
    animalForm.reset();

    document.getElementById("animalId").value = animal ? animal.id : "";
    document.getElementById("nome").value = animal ? animal.nome : "";
    document.getElementById("idCliente").value = animal ? animal.idCliente : "";
    document.getElementById("especie").value = animal ? animal.especie : "Cachorro";
    document.getElementById("raca").value = animal ? animal.raca : "";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    animalForm.reset();
}

async function loadAnimals() {
    renderLoading();
    setFeedback("");

    try {
        const records = await animalsService.list();
        animals = Array.isArray(records) ? records.map(normalizeAnimal) : [];
        renderAnimals();
    } catch (error) {
        animals = [];
        renderAnimals();
        setFeedback(
            "Nao foi possivel carregar os animais. Verifique se a API possui a rota /animais/listar.",
            "error"
        );
    }
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(animalForm);
    const animal = buildAnimalPayload(formData);

    try {
        await animalsService.save(animal);
        closeModal();
        await loadAnimals();
    } catch (error) {
        setFeedback("Nao foi possivel salvar o animal. Confira a API de animais.", "error");
    }
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = button.dataset.id;
    const animal = animals.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(animal);
        return;
    }

    if (!confirm("Excluir este animal?")) {
        return;
    }

    try {
        await animalsService.remove(id);
        await loadAnimals();
    } catch (error) {
        setFeedback("Nao foi possivel excluir o animal. Confira a API de animais.", "error");
    }
}

openModalButton.addEventListener("click", () => openModal());
closeModalButton.addEventListener("click", closeModal);
cancelModalButton.addEventListener("click", closeModal);
animalForm.addEventListener("submit", handleSubmit);
animalsTableBody.addEventListener("click", handleTableClick);

modalBackdrop.addEventListener("click", (event) => {
    if (event.target === modalBackdrop) {
        closeModal();
    }
});

loadAnimals();
