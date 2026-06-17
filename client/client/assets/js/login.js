const loginForm = document.getElementById("loginForm");
const errorMessage = document.getElementById("errorMessage");

loginForm.addEventListener("submit", (event) => {

    event.preventDefault();

    errorMessage.style.display = "none";

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    const validUsername = "admin";
    const validPassword = "admin";

    if (
        username !== validUsername ||
        password !== validPassword
    ) {

        errorMessage.textContent =
            "Usuario ou senha invalidos.";

        errorMessage.style.display = "block";

        return;
    }

    alert("Login realizado com sucesso.");

    window.location.href = "./atendimentos.html";
});
