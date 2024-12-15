package com.lab1.imports.dto.log;

import com.lab1.imports.Status;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportLogCreateDto {
    @NotNull
    private String fileKey;

    private Integer createdCount;

    @NotNull
    private Status status;
}
