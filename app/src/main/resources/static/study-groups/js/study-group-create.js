import { getCookie } from "/common/js/cookie.js"
import { showErrorMessage } from "/common/js/error.js"

export function validateAndGetStudyGroupData() {
    let name = document.getElementById("name").value.trim();
    if (name.length == 0) {
        throw "Name must not be empty";
    }
    let x = parseFloat(document.getElementById("x").value);
    let y = parseFloat(document.getElementById("y").value);
    let studentsCount = parseInt(document.getElementById("students-count").value, 10);
    if (studentsCount < 1) {
        throw "Student count must be >=1";
    }
    let expelledStudents = parseInt(document.getElementById("expelled-students").value, 10);
    if (expelledStudents < 1) {
        throw "Expelled students must be >=1";
    }
    let transferredStudents = parseInt(document.getElementById("transferred-students").value, 10);
    if (expelledStudents < 1) {
        throw "Transferred students must be >=1";
    }
    let formOfEducation = document.getElementById("form-of-education").value;
    let shouldBeExpelled = parseInt(document.getElementById("should-be-expelled").value, 10);
    if (shouldBeExpelled < 1) {
        throw "Students (that should be expelled) must be >=1";
    }
    let averageMark = parseInt(document.getElementById("average-mark").value, 10);
    if (averageMark < 1) {
        throw "Average mark must be >=1";
    }
    let semesterEnum = document.getElementById("semester-enum").value;
    let groupAdminId = parseInt(document.getElementById("group-admin-id").value, 10);
    if (groupAdminId < 0) {
        throw "Group admin ID must be a positive integer";
    }

    return {
        name: name,
        coordinates: {
            x: x,
            y: y,
        },
        studentsCount: studentsCount,
        expelledStudents: expelledStudents,
        transferredStudents: transferredStudents,
        formOfEducation: formOfEducation,
        shouldBeExpelled: shouldBeExpelled,
        averageMark: averageMark,
        semesterEnum: semesterEnum,
        groupAdminId: groupAdminId,
    };
}

function sendForm() {
    let data;
    try {
        data = validateAndGetStudyGroupData();
    } catch (err) {
        showErrorMessage(err);
        return;
    }

    console.log("Submitting Data:", JSON.stringify(data, null, 2));

    fetch("/api/study-groups", {
        method: "POST",
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
                throw responseData.message || "Failed to create Study Group"
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
