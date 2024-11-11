package com.lab1.studygroups.dto;

import com.lab1.studygroups.Coordinates;
import com.lab1.studygroups.FormOfEducation;
import com.lab1.studygroups.Semester;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupCreateDto {
    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private Coordinates coordinates;

    @Min(1)
    @NotNull
    private int studentsCount;

    @Min(1)
    @NotNull
    private Integer expelledStudents;

    @Min(1)
    @NotNull
    private int transferredStudents;

    @NotNull
    private FormOfEducation formOfEducation;

    @Min(1)
    @NotNull
    private int shouldBeExpelled;

    @Min(1)
    @NotNull
    private int averageMark;

    @NotNull
    private Semester semesterEnum;

    @NotNull
    private int groupAdminId;
}
