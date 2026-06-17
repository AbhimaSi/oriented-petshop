const API_BASE_URL = localStorage.getItem("javaPetApiBaseUrl") || "http://localhost:8080";

const employeesTableBody = document.getElementById("employeesTableBody");
const employeeFeedback = document.getElementById("employeeFeedback");
const openModalButton = document.getElementById("openModalButton");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModalButton");
const cancelModalButton = document.getElementById("cancelModalButton");
const employeeForm = document.getElementById("employeeForm");
const modalTitle = document.getElementById("modalTitle");

let employees = [];

const employeesService = {
    async list() {
        return apiRequest("/funcionarios/listar");
    },

    async save(employee) {
        if (employee.id) {
            return apiRequest(`/funcionarios/${employee.id}`, {
                method: "PUT",
                body: JSON.stringify(employee)
            });
        }

        return apiRequest("/funcionarios", {
            method: "POST",
            body: JSON.stringify(employee)
        });
    },

    async remove(id) {
        return apiRequest(`/funcionarios/${id}`, {
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

function normalizeEmployee(employee) {
    return {
        id: employee.id ? String(employee.id) : "",
        nome: employee.nome || "",
        cargo: employee.cargo || "",
        email: employee.email || "",
        senha: employee.senha || ""
    };
}

function buildEmployeePayload(formData) {
    const employee = Object.fromEntries(formData.entries());

    return {
        id: employee.id ? Number(employee.id) : null,
        nome: employee.nome.trim(),
        email: employee.email.trim(),
        senha: employee.senha,
        cargo: employee.cargo.trim()
    };
}

function setFeedback(message, type = "") {
    employeeFeedback.textContent = message;
    employeeFeedback.dataset.type = type;
}

function renderLoading() {
    employeesTableBody.innerHTML = `
        <tr>
            <td class="empty-row" colspan="4">Carregando funcionarios...</td>
        </tr>
    `;
}

function renderEmployees() {
    if (!employees.length) {
        employeesTableBody.innerHTML = `
            <tr>
                <td class="empty-row" colspan="4">Nenhum funcionario encontrado.</td>
            </tr>
        `;
        return;
    }

    employeesTableBody.innerHTML = employees.map((employee) => `
        <tr>
            <td>${employee.nome}</td>
            <td>${employee.cargo}</td>
            <td>${employee.email}</td>
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
    document.getElementById("email").value = employee ? employee.email : "";
    document.getElementById("senha").value = employee ? employee.senha : "";

    modalBackdrop.hidden = false;
}

function closeModal() {
    modalBackdrop.hidden = true;
    employeeForm.reset();
}

async function loadEmployees() {
    renderLoading();
    setFeedback("");

    try {
        const records = await employeesService.list();
        employees = Array.isArray(records) ? records.map(normalizeEmployee) : [];
        renderEmployees();
    } catch (error) {
        employees = [];
        renderEmployees();
        setFeedback(
            "Nao foi possivel carregar os funcionarios. Verifique se a API possui a rota /funcionarios/listar.",
            "error"
        );
    }
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(employeeForm);
    const employee = buildEmployeePayload(formData);

    try {
        await employeesService.save(employee);
        closeModal();
        await loadEmployees();
    } catch (error) {
        setFeedback("Nao foi possivel salvar o funcionario. Confira a API de funcionarios.", "error");
    }
}

async function handleTableClick(event) {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = button.dataset.id;
    const employee = employees.find((item) => item.id === id);

    if (button.dataset.action === "edit") {
        openModal(employee);
        return;
    }

    if (!confirm("Excluir este funcionario?")) {
        return;
    }

    try {
        await employeesService.remove(id);
        await loadEmployees();
    } catch (error) {
        setFeedback("Nao foi possivel excluir o funcionario. Confira a API de funcionarios.", "error");
    }
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

loadEmployees();
