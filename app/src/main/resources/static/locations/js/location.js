import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"
import { validateAndGetData } from "/locations/js/location-create.js"

var objectId;

function loadObjectIdFromPath() {
    const path = window.location.pathname;

    const match = path.match(/^\/locations\/(\d+)$/);

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
    fetch(`/api/locations/${objectId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        if (response.status == 404) {
            document.getElementById("locations-form").hidden = true;
        }

        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to fetch Location");
                return;
            }

            let currentUserId = localStorage.getItem("id");
            if (responseData.owner.id != currentUserId) {
                disableEditing();
            }

            document.getElementById("name").value = responseData.name;
            document.getElementById("owner-username").value = responseData.owner?.username || "";
            document.getElementById("created-at").value = responseData.createdAt;
            document.getElementById("updated-by-username").value = responseData.updatedBy?.username || "";
            document.getElementById("updated-at").value = responseData.updatedAt;
            document.getElementById("x").value = responseData.x;
            document.getElementById("y").value = responseData.y;
            document.getElementById("z").value = responseData.z;
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function deleteObject() {
    fetch(`/api/locations/${objectId}`, {
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
                showErrorMessage(responseData.message || "Failed to delete Location");
                return;
            }

            window.location.href = "/locations"
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

    fetch(`/api/locations/${objectId}`, {
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
                showErrorMessage(responseData.message || "Failed to update Location");
                return;
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

document.getElementById("button-delete").onclick = (e) => {
    deleteObject();
};

loadObjectIdFromPath();
loadObject();
