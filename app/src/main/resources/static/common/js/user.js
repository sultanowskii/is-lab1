export function isAdmin() {
    return localStorage.getItem("type") === "ADMIN";
}