import { isAdmin } from "/common/js/user.js"

export function errIfNotAdmin() {
    if (isAdmin()) {
        return;
    }
    alert("Admin only!");
    window.location.href = "/index";
}
