import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"
import { renderTable, flattenObjects } from "/common/js/table.js"
import { sampleObject } from "/study-groups/js/sample.js";

function findWithNameLike() {
    const namePart = document.getElementById("name-part").value;

    fetch(`/api/extra/study-groups/find-with-name-like?partialName=${namePart}`, {
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

            const studyGroups = responseData;
            const flattenedStudyGroups = flattenObjects(studyGroups);
            renderTable(flattenedStudyGroups, "table-container", sampleObject, "/study-groups");
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

document.getElementById("button-find").onclick = (e) => {
    findWithNameLike();
};
