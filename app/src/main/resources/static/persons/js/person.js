import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"
import { isAdmin } from "/common/js/user.js"
import { validateAndGetData } from "/persons/js/person-create.js"

var objectId;

function loadObjectIdFromPath() {
    const path = window.location.pathname;

    const match = path.match(/^\/persons\/(\d+)$/);

    if (!match) {
        showErrorMessage("URL seems to be broken. Try to go back ");
        return;
    }
    
    objectId = parseInt(match[1], 10);
    if (isNaN(objectId)) {
        showErrorMessage("URL seems to be broken. Try to go back ");
        return;
    }
}

function disableEditing(disable = true) {
    let itemsToDisable = document.querySelectorAll(".form-input");
    itemsToDisable.forEach(item => {
        item.disabled = disable;
    })
}

function loadObject() {
    fetch(`/api/persons/${objectId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        if (response.status == 404) {
            document.getElementById("person-form").hidden = true;
        }

        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to fetch Person");
                return;
            }

            let currentUserId = localStorage.getItem("id");
            if (!isAdmin() && responseData.owner.id != currentUserId) {
                disableEditing();
            }

            document.getElementById("name").value = responseData.name;
            document.getElementById("owner-username").value = responseData.owner?.username || "";
            document.getElementById("created-at").value = responseData.createdAt;
            document.getElementById("updated-by-username").value = responseData.updatedBy?.username || "";
            document.getElementById("updated-at").value = responseData.updatedAt;
            document.getElementById("eye-color").value = responseData.eyeColor;
            document.getElementById("hair-color").value = responseData.hairColor;
            document.getElementById("location-id").value = responseData.locationId;
            document.getElementById("height").value = responseData.height;
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function deleteObject() {
    fetch(`/api/persons/${objectId}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to delete Person");
                return;
            }

            window.location.href = "/persons"
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function sendForm() {
    let data;
    try {
        data = validateAndGetData();
    } catch (err) {
        showErrorMessage(err);
        return;
    }

    fetch(`/api/persons/${objectId}`, {
        method: "PUT",
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
                showErrorMessage(responseData.message || "Failed to update Person");
                return;
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

document.getElementById("button-delete").onclick = (e) => {
    deleteObject();
};

loadObjectIdFromPath();
loadObject();
