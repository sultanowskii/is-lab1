package com.lab1.common.dto;

import com.lab1.common.error.BadRequestException;

import lombok.Data;

@Data
public class SearchParamsDto {
    String searchFieldName;
    String searchString;

    public void validate() throws BadRequestException {
        if (!isValid()) {
            throw new BadRequestException("Params 'searchFieldName' and 'searchString' work in pair - you must use either both or none");
        }
    }

    public boolean isValid() {
        return (searchFieldName == null && searchString == null) || (searchFieldName != null && searchString != null);
    }

    public boolean isEmpty() {
        return searchFieldName == null && searchString == null;
    }
}
