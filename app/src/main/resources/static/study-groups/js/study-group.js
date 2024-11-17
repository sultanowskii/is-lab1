import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"
import { validateAndGetStudyGroupData } from "/study-groups/js/study-group-create.js"

var objectId;

function loadObjectIdFromPath() {
    const path = window.location.pathname;

    const match = path.match(/^\/study-groups\/(\d+)$/);

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
    fetch(`/api/study-groups/${objectId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        if (response.status == 404) {
            document.getElementById("study-group-form").hidden = true;
        }

        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to fetch Study Group");
                return;
            }

            let currentUserId = localStorage.getItem("id");
            if (responseData.owner.id != currentUserId) {
                disableEditing();
            }

            document.getElementById("name").value = responseData.name;
            document.getElementById("owner-username").value = responseData.owner.username;
            document.getElementById("created-at").value = responseData.createdAt;
            document.getElementById("updated-by-username").value = responseData.updatedBy.username;
            document.getElementById("updated-at").value = responseData.updatedAt;
            document.getElementById("x").value = responseData.coordinates.x;
            document.getElementById("y").value = responseData.coordinates.y;
            document.getElementById("students-count").value = responseData.studentsCount;
            document.getElementById("expelled-students").value = responseData.expelledStudents;
            document.getElementById("transferred-students").value = responseData.transferredStudents;
            document.getElementById("form-of-education").value = responseData.formOfEducation;
            document.getElementById("should-be-expelled").value = responseData.shouldBeExpelled;
            document.getElementById("average-mark").value = responseData.averageMark;
            document.getElementById("semester-enum").value = responseData.semesterEnum;
            document.getElementById("group-admin-id").value = responseData.groupAdminId;
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function deleteObject() {
    fetch(`/api/study-groups/${objectId}`, {
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
                showErrorMessage(responseData.message || "Failed to delete Study Group");
                return;
            }

            window.location.href = "/study-groups"
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function sendForm() {
    let data;
    try {
        data = validateAndGetStudyGroupData();
    } catch (err) {
        showErrorMessage(err);
        return;
    }

    fetch(`/api/study-groups/${objectId}`, {
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
                showErrorMessage(responseData.message || "Failed to update Study Group");
                return;
            }
            window.location.href = `/study-groups/${responseData.id}`
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

document.getElementById("study-group-form").onsubmit = (e) => {
    e.preventDefault();
    sendForm();
};

document.getElementById("button-delete").onclick = (e) => {
    deleteObject();
};

loadObjectIdFromPath();
loadObject();
