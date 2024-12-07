package com.lab1.imports.dto;

import com.lab1.imports.Status;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportCreateDto {
    @NotNull
    private int createdCount;

    @NotNull
    private Status status;
}
