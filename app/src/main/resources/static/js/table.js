function sortWithPreferenceToId(strings) {
    const tmp = strings.filter((s) => s !== "id");

    tmp.sort();

    if (strings.includes("id")) {
        return ["id"].concat(tmp);
    }
    return tmp;
}

export function renderTable(data, containerId, sampleObject) {
    const container = document.getElementById(containerId);
    if (!container) {
        console.error(`Container with id "${containerId}" not found.`);
        return;
    }

    // Create the table element
    const table = document.createElement("table");
    table.style.borderCollapse = "collapse";
    table.style.width = "100%";
    table.style.margin = "20px 0";

    // Generate table headers
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

    // Generate table rows
    const tbody = document.createElement("tbody");
    data.forEach(row => {
        const tr = document.createElement("tr");

        headers.forEach(header => {
            const td = document.createElement("td");
            td.textContent = row[header];
            td.style.border = "1px solid #ddd";
            td.style.padding = "8px";
            tr.appendChild(td);
        });

        tbody.appendChild(tr);
    });

    table.appendChild(tbody);

    // Clear existing content and append the new table
    container.innerHTML = "";
    container.appendChild(table);
}

export function flattenObjects(array) {
    return array.map(flattenObject);
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