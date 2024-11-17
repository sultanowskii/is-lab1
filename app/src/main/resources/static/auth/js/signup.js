import { setCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

function signup() {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const userType = document.getElementById("user-type").value.trim();

    if (!username || !password || !userType) {
        showErrorMessage("Username and password are required.");
        return;
    }

    fetch(
        "/auth/signup",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password }),
        },
    )
    .then(response => {
        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Sign in failed.");
                return;
            }

            setCookie("authToken", responseData.token, 10);
            localStorage.setItem("id", parseInt(responseData.user.id, 10));
            localStorage.setItem("username", responseData.user.username);
            localStorage.setItem("type", responseData.user.type);

            window.location.href = "/index.html";
        })
    })
    .catch(err => {
        showErrorMessage("An error occurred" + err + ". Please try again.");
    });
}

document.getElementById("signup-button").onlick = signup;
