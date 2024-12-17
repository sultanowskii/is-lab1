function sortWithPreferenceToId(strings) {
    const tmp = strings.filter((s) => s !== "id");

    tmp.sort();

    if (strings.includes("id")) {
        return ["id"].concat(tmp);
    }
    return tmp;
}

export function renderTable(data, containerId, sampleObject, resourcePath) {
    const container = document.getElementById(containerId);

    const table = document.createElement("table");
    table.style.borderCollapse = "collapse";
    table.style.width = "100%";
    table.style.margin = "20px 0";

    const headers = sortWithPreferenceToId(Object.keys(sampleObject));
    const thead = document.createElement("thead");
    const headerRow = document.createElement("tr");

    headers.forEach(header => {
        const th = document.createElement("th");
        th.textContent = header;
        th.style.border = "1px solid #ddd";
        th.style.padding = "8px";
        th.style.backgroundColor = "#f2f2f2";
        th.style.textAlign = "left";
        headerRow.appendChild(th);
    });

    thead.appendChild(headerRow);
    table.appendChild(thead);

    const tbody = document.createElement("tbody");
    data.forEach(row => {
        const tr = document.createElement("tr");

        headers.forEach(header => {
            const td = document.createElement("td");
            td.style.border = "1px solid #ddd";
            td.style.padding = "8px";
            if (header == "id") {
                const id = row[header];
                td.innerHTML = `<a href="${resourcePath}/${id}">${id}</a>`;
            } else {
                td.textContent = row[header];
            }
            tr.appendChild(td);
        });

        tbody.appendChild(tr);
    });

    table.appendChild(tbody);

    container.innerHTML = "";
    container.appendChild(table);
}

export function flattenObjects(array) {
    return array.map((v) => flattenObject(v));
}

export function flattenObject(obj, parentKey = "") {
    const result = {};

    for (const [key, value] of Object.entries(obj)) {
        const newKey = parentKey ? `${parentKey}.${key}` : key;

        if (value !== null && typeof value === "object" && !Array.isArray(value)) {
            // Рекурсивно обрабатываем вложенные объекты
            Object.assign(result, flattenObject(value, newKey));
        } else {
            // Примитивные значения добавляем без изменений
            result[newKey] = value;
        }
    }

    return result;
}