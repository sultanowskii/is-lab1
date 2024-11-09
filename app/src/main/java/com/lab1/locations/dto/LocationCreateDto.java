package com.lab1.locations.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationCreateDto {
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
