package com.lab1.common.dto;

import com.lab1.common.error.ValidationException;

import lombok.Data;

@Data
public class SearchParamsDto {
    String searchFieldName;
    String searchString;

    public void validate() throws ValidationException {
        if (!isValid()) {
            throw new ValidationException("Params 'searchFieldName' and 'searchString' work in pair - you must use either both or none");
        }
    }

    public boolean isValid() {
        return (searchFieldName == null && searchString == null) || (searchFieldName != null && searchString != null);
    }

    public boolean isEmpty() {
        return searchFieldName == null && searchString == null;
    }
}
