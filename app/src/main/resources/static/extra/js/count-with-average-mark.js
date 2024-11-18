import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

function countWithAverageMark() {
    let averageMark = parseInt(document.getElementById("average-mark").value, 10);
    if (averageMark < 1) {
        showErrorMessage("Average mark must be >=1");
    }

    fetch(`/api/extra/study-groups/count-with-average-mark?averageMark=${averageMark}`, {
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

            document.getElementById("result").textContent = `Count: ${responseData.count}`;
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

document.getElementById("button-count").onclick = (e) => {
    countWithAverageMark();
};
