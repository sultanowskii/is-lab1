import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

export function validateAndGetData() {
    let name = document.getElementById("name").value.trim();
    if (name.length == 0) {
        throw "Name must not be empty";
    }
    let eyeColor = document.getElementById("eye-color").value;
    let hairColor = document.getElementById("hair-color").value;
    let locationId = parseInt(document.getElementById("location-id").value, 10);
    if (locationId < 0) {
        throw "Group admin ID must be a positive integer";
    }
    let height = parseInt(document.getElementById("height").value, 10);
    if (height < 1) {
        throw "Height must be >=1";
    }

    return {
        name: name,
        eyeColor: eyeColor,
        hairColor: hairColor,
        locationId: locationId,
        height: height,
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

    console.log("Submitting Data:", JSON.stringify(data, null, 2));

    fetch("/api/persons", {
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
                throw responseData.message || "Failed to create Person"
            }
            window.location.href = `/persons/${responseData.id}`
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}


document.getElementById("person-form").onsubmit = (e) => {
    e.preventDefault();
    sendForm();
};
