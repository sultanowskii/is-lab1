package com.lab1.studygroups;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
public enum FormOfEducation {
    DISTANCE_EDUCATION,
    FULL_TIME_EDUCATION,
    EVENING_CLASSES;

    @Converter(autoApply = true)
    public static class FormOfEducationConverter implements AttributeConverter<FormOfEducation, String> {
        @Override
        public String convertToDatabaseColumn(FormOfEducation formOfEducation) {
            return formOfEducation.name();
        }

        @Override
        public FormOfEducation convertToEntityAttribute(String dbData) {
            return FormOfEducation.valueOf(dbData);
        }
    }
}