package com.lab1.common;

import org.springframework.data.jpa.domain.Specification;

import com.lab1.common.dto.SearchParamsDto;
import com.lab1.common.error.BadRequestException;

public abstract class CRUDSpecification<T extends OwnedEntity> {
    public abstract boolean isFieldValid(String fieldName);


    public Specification<T> build(SearchParamsDto searchParamsDto) throws BadRequestException {
        return withFieldContaining(searchParamsDto.getSearchFieldName(), searchParamsDto.getSearchString());
    }

    public Specification<T> withFieldContaining(String fieldName, String substring) throws BadRequestException {
        if (!isFieldValid(fieldName)) {
            throw new BadRequestException("Field '" + fieldName + "' is not a valid searchable field");
        }

        return (root, query, builder) -> {
            return builder.like(builder.lower(root.get(fieldName)), "%" + substring.toLowerCase() + "%");
        };
    }
}
