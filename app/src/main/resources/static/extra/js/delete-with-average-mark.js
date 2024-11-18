import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

function deleteWithAverageMark() {
    let averageMark = parseInt(document.getElementById("average-mark").value, 10);
    if (averageMark < 1) {
        showErrorMessage("Average mark must be >=1");
    }

    fetch(`/api/extra/study-groups/delete-with-average-mark`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
        body: JSON.stringify({
            averageMark: averageMark,
        })
    })
    .then(response => {
        if (!response.ok) {
            showErrorMessage(response.message || "Failed to delete");
            return;
        }

        alert("Successfully deleted");
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

document.getElementById("button-delete").onclick = (e) => {
    deleteWithAverageMark();
};
