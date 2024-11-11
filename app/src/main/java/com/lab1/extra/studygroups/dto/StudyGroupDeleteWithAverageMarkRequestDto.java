package com.lab1.extra.studygroups.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudyGroupDeleteWithAverageMarkRequestDto {
    @NotNull
    int averageMark;
}
