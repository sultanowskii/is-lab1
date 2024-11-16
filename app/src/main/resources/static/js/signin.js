import { setCookie } from "/js/cookie.js"
import { showErrorMessage } from "/js/error.js"

function initPage() {
    const signinButton = document.getElementById("signin-button");

    signinButton.addEventListener("click", async () => {
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();

        if (!username || !password) {
            showErrorMessage("Username and password are required.");
            return;
        }

        try {
            const response = await fetch(
                "/auth/signin",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ username, password }),
                },
            );

            if (!response.ok) {
                const errorData = await response.json();
                showErrorMessage(errorData.message || "Sign in failed.");
                return;
            }

            const data = await response.json();
            const token = data.token;

            setCookie("authToken", token, 10);

            window.location.href = "/index.html";
        } catch (error) {
            console.error("Sign in error:", error);
            showErrorMessage("An error occurred. Please try again.");
        }
    });
}

initPage();
