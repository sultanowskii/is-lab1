package com.lab1.studygroups.dto;

import com.lab1.persons.Person;
import com.lab1.studygroups.*;
import lombok.Data;

@Data
public class StudyGroupDto {
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
    private Person groupAdmin;
}
