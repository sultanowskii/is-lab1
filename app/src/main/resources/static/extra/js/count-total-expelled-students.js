import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

function countTotalExpelledStudents() {
    fetch(`/api/extra/study-groups/count-total-expelled-students`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${getCookie("authToken")}`,
        },
    })
    .then(response => {
        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to count");
                return;
            }

            document.getElementById("result").textContent = `Count: ${responseData.totalExpelledStudentCount}`;
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

document.getElementById("button-count").onclick = (e) => {
    countTotalExpelledStudents();
};
