import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

export function getData() {
    const fileInput = document.getElementById('file');
    const file = fileInput.files[0];

    if (!file) {
        alert('Please select a file!');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    return formData;
}

function sendForm() {
    let formData = getData();

    fetch("/api/imports", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
        body: formData,
    })
    .then(
        response => {
            if (!response.ok) {
                response
                    .json()
                    .then(
                        responseData => {
                            throw responseData.message || "Failed to import"
                        }
                    )
                    .catch(err => {
                        showErrorMessage(err);
                    })
            } else {
                window.location.href = `/imports`
            }
        }
    )
    .catch(err => {
        showErrorMessage(err.message);
    });
}


document.getElementById("import-form").onsubmit = (e) => {
    e.preventDefault();
    sendForm();
};
