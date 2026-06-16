const loginForm = document.getElementById("loginForm");
const errorMessage = document.getElementById("errorMessage");

loginForm.addEventListener("submit", async (event) => {

    event.preventDefault();

    errorMessage.style.display = "none";

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    try {

        /*
        Futuramente:

        const response = await login(email, password);

        if (!response.ok) {
            throw new Error();
        }

        */

        const validEmail = "admin@petshop.com";
        const validPassword = "123456";

        if (
            email !== validEmail ||
            password !== validPassword
        ) {
            throw new Error();
        }

        alert("Login realizado com sucesso.");

        window.location.href = "./atendimentos.html";

    } catch {

        errorMessage.textContent =
            "Email ou senha invalidos.";

        errorMessage.style.display = "block";
    }
});
