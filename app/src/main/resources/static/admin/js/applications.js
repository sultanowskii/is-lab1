import { renderTable, flattenObjects, flattenObject } from "/common/js/table.js"
import { showErrorMessage } from "/common/js/error.js"
import { getCookie } from "/common/js/cookie.js"
import { errIfNotAdmin } from "/admin/js/admin-check.js"

var pageNumber = 1;
var totalPages = 1;

const sampleObject = flattenObject({
    "id": 0,
    "user": {
        "id": 0,
        "username": "",
        "type": "",
    },
    "status": "",
});

function buildQueryParams() {
    const pageSize = parseInt(document.getElementById("page-size").value.trim());
    
    if (pageSize < 0) {
        throw "Page size must a non-negative number.";
    }

    const pageIndex = pageNumber - 1;
    let params = `page=${pageIndex}&size=${pageSize}`;

    return params;
}

function fetchAndRenderApplications() {
    let params;
    try {
        params = buildQueryParams();
    } catch (e) {
        showErrorMessage(e);
        return;
    }

    fetch(
        `/api/admin/applications?${params}`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${getCookie("authToken")}`,
            },
        },
    )
    .then(response => {
        response.json()
        .then(responseData => {
            if (!response.ok) {
                showErrorMessage(responseData.message || "Failed to fetch applications.");
                return;
            }

            const applications = responseData.content
            const flattenedApplications = flattenObjects(applications);
            totalPages = responseData.page.totalPages;

            renderTable(flattenedApplications, "table-container", sampleObject, "/admin/applications");
        })
    })
    .catch(err => {
        showErrorMessage(err.message);
    });
}

function updatePageNumberTextContent() {
    document.getElementById("page-number-text").textContent = `Page #${pageNumber}`
}

function updatePage() {
    updatePageNumberTextContent();
    fetchAndRenderApplications();
}

function setUpdatePageTriggers() {
    const inputs = [
        "page-size",
    ];

    inputs.forEach((v) => {
        document.getElementById(v).onchange = updatePage;
    })
}

document.getElementById("previous-page").onclick = (event) => {
    if (pageNumber <= 1) {
        return;
    }
    pageNumber--;
    updatePage();
};


document.getElementById("next-page").onclick = (event) => {
    if (pageNumber >= totalPages) {
        return;
    }
    pageNumber++;
    updatePage();
};

function pollApplications() {
    fetchAndRenderApplications();
    setTimeout(pollApplications, 3 * 1000);
}

errIfNotAdmin();
pollApplications();
updatePageNumberTextContent();
setUpdatePageTriggers();
