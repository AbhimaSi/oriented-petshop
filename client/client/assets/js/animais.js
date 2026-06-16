const initialAnimals = [
    {
        id: 1,
        nome: "Mel",
        tutor: "Mariana Costa",
        especie: "Cachorro",
        raca: "Golden Retriever",
        idade: "4 anos",
        peso: "28 kg"
    },
    {
        id: 2,
        nome: "Tom",
        tutor: "Lucas Ferreira",
        especie: "Gato",
        raca: "SRD",
        idade: "2 anos",
        peso: "5 kg"
    },
    {
        id: 3,
        nome: "Luna",
        tutor: "Ana Souza",
        especie: "Cachorro",
        raca: "Poodle",
        idade: "6 anos",
        peso: "8 kg"
    }
];

const animalsTableBody = document.getElementById("animalsTableBody");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const animalForm = document.getElementById("animalForm");
const modalTitle = document.getElementById("modalTitle");

let animals = [...initialAnimals];

const animalsService = {
    async list() {
        return [...animals];
    },

    async save(animal) {
        if (animal.id) {
            animals = animals.map((item) =>
                item.id === animal.id ? animal : item
            );

            return animal;
        }

        const nextId = animals.length
            ? Math.max(...animals.map((item) => item.id)) + 1
            : 1;

        const createdAnimal = {
            ...animal,
            id: nextId
        };

        animals.push(createdAnimal);
        return createdAnimal;
    },

    async remove(id) {
        animals = animals.filter((animal) => animal.id !== id);
    }
};

function renderAnimals() {
    if (!animals.length) {
        animalsTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="7">Nenhum animal encontrado.</td>
            </tr>
        `;
        return;
    }

    animalsTableBody.innerHTML = animals.map((animal) => `
        <tr>
            <td>${animal.nome}</td>
            <td>${animal.tutor}</td>
            <td>${animal.especie}</td>
            <td>${animal.raca}</td>
            <td>${animal.idade}</td>
            <td>${animal.peso}</td>
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
    document.getElementById("tutor").value = animal ? animal.tutor : "";
    document.getElementById("especie").value = animal ? animal.especie : "Cachorro";
    document.getElementById("raca").value = animal ? animal.raca : "";
    document.getElementById("idade").value = animal ? animal.idade : "";
    document.getElementById("peso").value = animal ? animal.peso : "";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    animalForm.reset();
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(animalForm);
    const animal = Object.fromEntries(formData.entries());

    await animalsService.save({
        ...animal,
        id: animal.id ? Number(animal.id) : null
    });

    closeModal();
    renderAnimals();
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    const animal = animals.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(animal);
        return;
    }

    await animalsService.remove(id);
    renderAnimals();
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

animalsService.list().then((records) => {
    animals = records;
    renderAnimals();
});
