package com.lab1.admin;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.lab1.common.error.ValidationException;

@Component
public class AdminApplicationSpecification {
    public Specification<AdminApplication> withOnlyCreated() throws ValidationException {
        return (root, query, builder) -> {
            return builder.equal(root.get("status"), AdminApplicationStatus.CREATED.toString());
        };
    }
}
