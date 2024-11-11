package com.lab1.extra.studygroups.dto;

import com.lab1.studygroups.FormOfEducation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudyGroupChangeFormOfEducationToRequestDto {
    @NotNull
    int id;

    @NotNull
    @NotBlank
    FormOfEducation formOfEducation;
}
