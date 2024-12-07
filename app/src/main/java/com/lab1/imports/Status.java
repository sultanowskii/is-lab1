package com.lab1.imports;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
public enum Status {
    SUCCESS,
    FAIL;

    @Converter(autoApply = true)
    public static class StatusConverter implements AttributeConverter<Status, String> {
        @Override
        public String convertToDatabaseColumn(Status formOfEducation) {
            return formOfEducation.name();
        }

        @Override
        public Status convertToEntityAttribute(String dbData) {
            return Status.valueOf(dbData);
        }
    }
}
