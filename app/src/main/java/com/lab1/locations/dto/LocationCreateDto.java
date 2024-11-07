package com.lab1.locations.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
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
