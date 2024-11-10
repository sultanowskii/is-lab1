package com.lab1.studygroups;

import com.lab1.common.CRUDSpecification;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class StudyGroupSpecification extends CRUDSpecification<StudyGroup> {
    private static final Set<String> VALID_SEARCH_FIELDS = Set.of("name", "form_of_education", "semester");

    @Override
    public boolean isFieldValid(String fieldName) {
        return VALID_SEARCH_FIELDS.contains(fieldName);
    }
}
