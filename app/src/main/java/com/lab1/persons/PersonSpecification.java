package com.lab1.persons;

import com.lab1.common.CRUDSpecification;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class PersonSpecification extends CRUDSpecification<Person> {
    private static final Set<String> VALID_SEARCH_FIELDS = Set.of("name", "eyeColor", "hairColor");

    @Override
    public boolean isFieldValid(String fieldName) {
        return VALID_SEARCH_FIELDS.contains(fieldName);
    }
}
