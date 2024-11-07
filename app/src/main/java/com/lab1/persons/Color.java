package com.lab1.persons;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;

@Getter
public enum Color {
    YELLOW,
    ORANGE,
    WHITE;

    @Converter(autoApply = true)
    public static class ColorConverter implements AttributeConverter<Color, String> {
        @Override
        public String convertToDatabaseColumn(Color color) {
            return color.name();
        }

        @Override
        public Color convertToEntityAttribute(String dbData) {
            return Color.valueOf(dbData);
        }
    }
}
