package com.lab1.imports;

import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.lab1.common.dto.SearchParamsDto;
import com.lab1.common.error.ValidationException;
import com.lab1.users.User;

@Component
public class ImportSpecification {
    private static final Set<String> VALID_SEARCH_FIELDS = Set.of("status");

    private boolean isFieldValid(String fieldName) {
        return VALID_SEARCH_FIELDS.contains(fieldName);
    }

    public Specification<Import> build(User user) throws ValidationException {
        if (user.isAdmin()) {
            return null;
        }
        return withPerformer(user);
    }

    public Specification<Import> buildWithSearchParams(User user, SearchParamsDto searchParamsDto) throws ValidationException {
        if (user.isAdmin()) {
            return withFieldContaining(searchParamsDto.getSearchFieldName(), searchParamsDto.getSearchString());
        }

        return Specification
            .where(withFieldContaining(searchParamsDto.getSearchFieldName(), searchParamsDto.getSearchString()))
            .and(withPerformer(user));
    }

    public Specification<Import> withFieldContaining(String fieldName, String substring) throws ValidationException {
        if (!isFieldValid(fieldName)) {
            throw new ValidationException("Field '" + fieldName + "' is not a valid searchable field");
        }

        return (root, query, builder) -> {
            return builder .like(builder.lower(root.get(fieldName)), "%" + substring.toLowerCase() + "%");
        };
    }

    public Specification<Import> withPerformer(User user) {
        return (root, query, builder) -> {
            return builder.equal(root.get("performer_id"), user.getId());
        };
    }
}
