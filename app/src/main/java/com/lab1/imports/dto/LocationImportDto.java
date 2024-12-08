package com.lab1.imports.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationImportDto {
    @NotNull
    private float x;

    @NotNull
    private float y;

    @NotNull
    private double z;

    @NotNull
    @Size(max = 694)
    private String name;
}
