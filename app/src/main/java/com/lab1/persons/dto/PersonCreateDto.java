package com.lab1.persons.dto;

import com.lab1.persons.Color;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
