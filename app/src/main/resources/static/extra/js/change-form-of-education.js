import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

function changeFormOfEducation() {
    let id = parseInt(document.getElementById("id").value, 10);
    if (id < 0) {
        throw "ID must be a positive integer";
    }
    let formOfEducation = document.getElementById("form-of-education").value;

    fetch(`/api/extra/study-groups/change-form-of-education`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
        body: JSON.stringify({
            id: id,
            formOfEducation: formOfEducation,
        }),
    })
    .then(response => {
        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to count");
                return;
            }

            alert("Successfully changed");
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

document.getElementById("button-change").onclick = (e) => {
    changeFormOfEducation();
};
