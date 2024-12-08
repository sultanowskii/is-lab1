package com.lab1.imports.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudyGroupsImportDto {
    @NotNull
    @Valid
    private List<StudyGroupImportDto> objects;
}
