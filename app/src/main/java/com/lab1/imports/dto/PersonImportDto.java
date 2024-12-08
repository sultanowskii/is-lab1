package com.lab1.imports.dto;

import com.lab1.persons.Color;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonImportDto {
    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private Color eyeColor;

    @NotNull
    private Color hairColor;

    @NotNull
    private LocationImportDto location;

    @Min(1)
    @NotNull
    private Integer height;
}
