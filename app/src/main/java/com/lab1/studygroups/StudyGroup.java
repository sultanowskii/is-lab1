package com.lab1.studygroups;

import com.lab1.common.Owned;
import com.lab1.persons.Person;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroup extends Owned implements com.lab1.common.Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private java.time.LocalDateTime creationDate;

    @Min(1)
    @Column(name = "students_count", nullable = false)
    private int studentsCount;

    @Min(1)
    @Column(name = "expelled_students", nullable = false)
    private Integer expelledStudents;

    @Min(1)
    @Column(name = "transferred_students", nullable = false)
    private int transferredStudents;

    @Enumerated(EnumType.STRING)
    @Column(name = "form_of_education", nullable = false)
    private FormOfEducation formOfEducation;

    @Min(1)
    @Column(name = "should_be_expelled", nullable = false)
    private int shouldBeExpelled;

    @Min(1)
    @Column(name = "average_mark", nullable = false)
    private int averageMark;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester", nullable = false)
    private Semester semesterEnum;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_admin_id", nullable = false)
    private Person groupAdmin;
}
