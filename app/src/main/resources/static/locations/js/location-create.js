import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

export function validateAndGetData() {
    let name = document.getElementById("name").value.trim();
    if (name.length == 0) {
        throw "Name must not be empty";
    }
    if (name.length > 694) {
        throw "Name is too long";
    }
    let x = parseInt(document.getElementById("x").value, 10);
    let y = parseInt(document.getElementById("y").value, 10);
    let z = parseInt(document.getElementById("z").value, 10);

    return {
        name: name,
        x: x,
        y: y,
        z: z,
    };
}

function sendForm() {
    let data;
    try {
        data = validateAndGetData();
    } catch (err) {
        showErrorMessage(err);
        return;
    }

    fetch("/api/locations", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
        body: JSON.stringify(data),
    })
    .then(response => {
        response.json()
        .then(responseData => {
            if (!response.ok) {
                throw responseData.message || "Failed to create Location"
            }
            window.location.href = `/locations/${responseData.id}`
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}


document.getElementById("location-form").onsubmit = (e) => {
    e.preventDefault();
    sendForm();
};
