package com.lab1.persons.dto;

import com.lab1.persons.Color;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PersonCreateDto {
    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private Color eyeColor;

    @NotNull
    private Color hairColor;

    @NotNull
    private int locationId;

    @Min(1)
    @NotNull
    private Integer height;
}
