export function showErrorMessage(message) {
    const errorMessage = document.getElementById("error-message");

    errorMessage.textContent = message;
    errorMessage.style.display = "block";
}
