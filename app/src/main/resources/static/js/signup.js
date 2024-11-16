import { setCookie } from "/js/cookie.js"
import { showErrorMessage } from "/js/error.js"

function initPage() {
    const signupButton = document.getElementById("signup-button");

    signupButton.addEventListener("click", async () => {
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();
        const userType = document.getElementById("user-type").value.trim();

        if (!username || !password || !userType) {
            showErrorMessage("Username, password and user type are required.");
            return;
        }

        try {
            const response = await fetch(
                "/auth/signup",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ username, password, userType }),
                },
            );

            if (!response.ok) {
                const errorData = await response.json();
                showErrorMessage(errorData.message || "Sign up failed.");
                return;
            }

            const data = await response.json();
            const token = data.token;

            setCookie("authToken", token, 10);

            window.location.href = "/index.html";
        } catch (error) {
            console.error("Sign up error:", error);
            showErrorMessage("An error occurred. Please try again.");
        }
    });
}

initPage();
