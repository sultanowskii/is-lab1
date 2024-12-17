import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

var objectId;

function loadObjectIdFromPath() {
    const path = window.location.pathname;

    const match = path.match(/^\/imports\/(\d+)$/);

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

function loadObject() {
    fetch(`/api/imports/${objectId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        if (response.status == 404) {
            document.getElementById("import-form").hidden = true;
        }

        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to fetch Import");
                return;
            }

            document.getElementById("performer-username").value = responseData.performer?.username || "";
            document.getElementById("status").value = responseData.status;
            document.getElementById("created-count").value = responseData.createdCount;

            if (responseData.status == "FAIL") {
                document.getElementById("button-download").hidden = true;
            }
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function downloadObject() {
    fetch(`/api/imports/${objectId}/download`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        if (!response.ok) {
            response.json()
            .then(responseData => {
                showErrorMessage(responseData.message || "Can't download import file");
                return;
            })
        }

        response.blob()
        .then(blob => {
            const tmp = document.createElement('a');
            tmp.href = window.URL.createObjectURL(blob);
            tmp.download = "archive.tar.gz";
    
            // Кликаем по ссылке, чтобы начать скачивание
            document.body.appendChild(tmp);
            tmp.click();
            document.body.removeChild(tmp);
        });
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

document.getElementById("button-download").onclick = (e) => {
    downloadObject();
};

loadObjectIdFromPath();
loadObject();
