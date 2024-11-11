package com.lab1.studygroups.dto;

import com.lab1.common.dto.OwnedDto;
import com.lab1.studygroups.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudyGroupDto extends OwnedDto {
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private java.time.LocalDateTime creationDate;
    private int studentsCount;
    private Integer expelledStudents;
    private int transferredStudents;
    private FormOfEducation formOfEducation;
    private int shouldBeExpelled;
    private int averageMark;
    private Semester semesterEnum;
    private int groupAdminId;
}
