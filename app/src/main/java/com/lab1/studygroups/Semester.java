package com.lab1.studygroups;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;

@Getter
public enum Semester {
    SECOND,
    FOURTH,
    EIGHTH;

    @Converter(autoApply = true)
    public static class SemesterConverter implements AttributeConverter<Semester, String> {
        @Override
        public String convertToDatabaseColumn(Semester semester) {
            return semester.name();
        }

        @Override
        public Semester convertToEntityAttribute(String dbData) {
            return Semester.valueOf(dbData);
        }
    }
}
