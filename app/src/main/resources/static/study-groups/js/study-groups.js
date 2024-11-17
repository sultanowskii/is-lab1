import { renderTable, flattenObjects, flattenObject } from "/common/js/table.js"
import { showErrorMessage } from "/common/js/error.js"
import { getCookie } from "/common/js/cookie.js"

var pageNumber = 1;
var totalPages = 1;

const sampleObject = flattenObject({
    "owner": {
        "id": 0,
        "username": "",
        "type": "",
    },
    "createdAt": "",
    "updatedBy": {
        "id": 0,
        "username": "",
        "type": "",
    },
    "updatedAt": "",
    "id": 0,
    "name": "b1234",
    "coordinates": {
        "x": 0,
        "y": 0,
    },
    "creationDate": "",
    "studentsCount": 1,
    "expelledStudents": 1,
    "transferredStudents": 1,
    "formOfEducation": "FULL_TIME_EDUCATION",
    "shouldBeExpelled": 1,
    "averageMark": 1,
    "semesterEnum": "SECOND",
    "groupAdminId": 0,
});


function getSelectedSortFields() {
    const checkboxSection = document.getElementById("sort-section");
    const checkboxes = checkboxSection.querySelectorAll('.checkbox-item-input');
    return Array.from(checkboxes).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);
}

function getSearchField() {
    const searchField = document.getElementById("search-field").value;
    return searchField || null;
}

function getSearchPrompt() {
    const searchPrompt = document.getElementById("search-prompt").value;
    return searchPrompt || null;
}

function buildQueryParams() {
    const pageSize = parseInt(document.getElementById("page-size").value.trim());
    
    if (pageSize < 0) {
        throw "Page size must a non-negative number.";
    }

    const pageIndex = pageNumber - 1;
    let params = `page=${pageIndex}&size=${pageSize}`;

    const sortFields = getSelectedSortFields();
    sortFields.forEach((field) => {
        params += `&sort=${field}`
    });

    const searchField = getSearchField();
    if (searchField != null) {
        const searchPrompt = getSearchPrompt();
        if (searchPrompt != null) {
            params += `&searchFieldName=${searchField}&searchString=${searchPrompt}`;
        }
    }

    return params;
}

function fetchAndRenderStudyGroups() {
    let params;
    try {
        params = buildQueryParams();
    } catch (e) {
        showErrorMessage(e);
        return;
    }

    fetch(
        `/api/study-groups?${params}`,
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
                showErrorMessage(responseData.message || "Failed to fetch study groups.");
                return;
            }

            const studyGroups = responseData.content
            const flattenedStudyGroups = flattenObjects(studyGroups);
            totalPages = responseData.page.totalPages;

            renderTable(flattenedStudyGroups, "table-container", sampleObject, "/study-groups");
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
    fetchAndRenderStudyGroups();
}

function setUpdatePageTriggers() {
    const inputs = [
        "page-size",
        "search-field",
        "search-prompt",
        "checkbox-item-input-name",
        "checkbox-item-input-form-of-education",
        "checkbox-item-input-semester",
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

function pollStudyGroups() {
    fetchAndRenderStudyGroups();
    setTimeout(pollStudyGroups, 3 * 1000);
}

pollStudyGroups();
updatePageNumberTextContent();
setUpdatePageTriggers();