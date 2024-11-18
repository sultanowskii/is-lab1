import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"
import { errIfNotAdmin } from "/admin/js/admin-check.js"

var objectId;

function loadObjectIdFromPath() {
    const path = window.location.pathname;

    const match = path.match(/^\/admin\/applications\/(\d+)$/);

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
    fetch(`/api/admin/applications/${objectId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        if (response.status == 404) {
            document.getElementById("application-form").hidden = true;
        }

        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to fetch Application");
                return;
            }

            document.getElementById("user-id").value = responseData.user.id;
            document.getElementById("user-username").value = responseData.user.username;
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function approve() {
    fetch(`/api/admin/applications/${objectId}/approve`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to approve Application");
                return;
            }

            window.location.href = "/admin/applications"
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function reject() {
    fetch(`/api/admin/applications/${objectId}/reject`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to approve Application");
                return;
            }

            window.location.href = "/admin/applications"
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

document.getElementById("button-approve").onclick = (e) => {
    e.preventDefault();
    approve();
};

document.getElementById("button-reject").onclick = (e) => {
    e.preventDefault();
    reject();
};

errIfNotAdmin();
loadObjectIdFromPath();
loadObject();
