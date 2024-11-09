package com.lab1.admin;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
public enum AdminApplicationStatus {
    CREATED,
    APPROVED,
    REJECTED;

    @Converter(autoApply = true)
    public static class AdminApplicationStatusConverter implements AttributeConverter<AdminApplicationStatus, String> {
        @Override
        public String convertToDatabaseColumn(AdminApplicationStatus adminApplicationStatus) {
            return adminApplicationStatus.name();
        }

        @Override
        public AdminApplicationStatus convertToEntityAttribute(String dbData) {
            return AdminApplicationStatus.valueOf(dbData);
        }
    }
}
