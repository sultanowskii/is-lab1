package com.lab1.common;

import org.springframework.data.jpa.domain.Specification;

import com.lab1.common.dto.SearchParamsDto;
import com.lab1.common.error.ValidationException;
import com.lab1.users.User;

public abstract class CRUDSpecification<T extends OwnedEntity> {
    public abstract boolean isFieldValid(String fieldName);

    public Specification<T> build(User user) throws ValidationException {
        if (user.isAdmin()) {
            return null;
        }
        return withOwner(user);
    }

    public Specification<T> buildWithSearchParams(User user, SearchParamsDto searchParamsDto) throws ValidationException {
        if (user.isAdmin()) {
            return withFieldContaining(searchParamsDto.getSearchFieldName(), searchParamsDto.getSearchString());
        }

        return Specification
            .where(withFieldContaining(searchParamsDto.getSearchFieldName(), searchParamsDto.getSearchString()))
            .and(withOwner(user));
    }

    public Specification<T> withFieldContaining(String fieldName, String substring) throws ValidationException {
        if (!isFieldValid(fieldName)) {
            throw new ValidationException("Field '" + fieldName + "' is not a valid searchable field");
        }

        return (root, query, builder) -> {
            return builder .like(builder.lower(root.get(fieldName)), "%" + substring.toLowerCase() + "%");
        };
    }

    public Specification<T> withOwner(User user) {
        return (root, query, builder) -> {
            return builder.equal(root.get("owner"), user);
        };
    }
}
