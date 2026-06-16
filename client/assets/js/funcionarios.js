const initialEmployees = [
    {
        id: 1,
        nome: "Carlos Almeida",
        cargo: "Banhista",
        telefone: "(11) 95555-1234",
        email: "carlos@javapet.com",
        status: "Ativo"
    },
    {
        id: 2,
        nome: "Fernanda Lima",
        cargo: "Veterinaria",
        telefone: "(11) 94444-5678",
        email: "fernanda@javapet.com",
        status: "Ativo"
    },
    {
        id: 3,
        nome: "Rafael Santos",
        cargo: "Atendente",
        telefone: "(11) 93333-9012",
        email: "rafael@javapet.com",
        status: "Inativo"
    }
];

const employeesTableBody = document.getElementById("employeesTableBody");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const employeeForm = document.getElementById("employeeForm");
const modalTitle = document.getElementById("modalTitle");

let employees = [...initialEmployees];

const employeesService = {
    async list() {
        return [...employees];
    },

    async save(employee) {
        if (employee.id) {
            employees = employees.map((item) =>
                item.id === employee.id ? employee : item
            );

            return employee;
        }

        const nextId = employees.length
            ? Math.max(...employees.map((item) => item.id)) + 1
            : 1;

        const createdEmployee = {
            ...employee,
            id: nextId
        };

        employees.push(createdEmployee);
        return createdEmployee;
    },

    async remove(id) {
        employees = employees.filter((employee) => employee.id !== id);
    }
};

function getStatusClass(status) {
    return status === "Inativo" ? "danger" : "";
}

function renderEmployees() {
    if (!employees.length) {
        employeesTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="6">Nenhum funcionario encontrado.</td>
            </tr>
        `;
        return;
    }

    employeesTableBody.innerHTML = employees.map((employee) => `
        <tr>
            <td>${employee.nome}</td>
            <td>${employee.cargo}</td>
            <td>${employee.telefone}</td>
            <td>${employee.email}</td>
            <td>
                <span class="status-badge ${getStatusClass(employee.status)}">
                    ${employee.status}
                </span>
            </td>
            <td>
                <div class="table-actions">
                    <button class="small-button" data-action="edit" data-id="${employee.id}" type="button">
                        Editar
                    </button>
                    <button class="small-button" data-action="delete" data-id="${employee.id}" type="button">
                        Excluir
                    </button>
                </div>
            </td>
        </tr>
    `).join("");
}

function openModal(employee = null) {
    modalTitle.textContent = employee ? "Editar funcionario" : "Novo funcionario";
    employeeForm.reset();

    document.getElementById("employeeId").value = employee ? employee.id : "";
    document.getElementById("nome").value = employee ? employee.nome : "";
    document.getElementById("cargo").value = employee ? employee.cargo : "";
    document.getElementById("telefone").value = employee ? employee.telefone : "";
    document.getElementById("email").value = employee ? employee.email : "";
    document.getElementById("status").value = employee ? employee.status : "Ativo";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    employeeForm.reset();
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(employeeForm);
    const employee = Object.fromEntries(formData.entries());

    await employeesService.save({
        ...employee,
        id: employee.id ? Number(employee.id) : null
    });

    closeModal();
    renderEmployees();
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    const employee = employees.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(employee);
        return;
    }

    await employeesService.remove(id);
    renderEmployees();
}

openModalButton.addEventListener("click", () => openModal());
closeModalButton.addEventListener("click", closeModal);
cancelModalButton.addEventListener("click", closeModal);
employeeForm.addEventListener("submit", handleSubmit);
employeesTableBody.addEventListener("click", handleTableClick);

modalBackdrop.addEventListener("click", (event) => {
    if (event.target === modalBackdrop) {
        closeModal();
    }
});

employeesService.list().then((records) => {
    employees = records;
    renderEmployees();
});
