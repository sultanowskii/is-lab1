package com.lab1.studygroups;

import com.lab1.persons.Person;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

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
