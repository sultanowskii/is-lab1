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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false)
    @NotBlank
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Embedded
    private Coordinates coordinates; //Поле не может быть null

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false)
    @Min(1)
    private int studentsCount; //Значение поля должно быть больше 0

    @Column
    private Integer expelledStudents; //Значение поля должно быть больше 0, Поле может быть null

    @Column(nullable = false)
    @Min(1)
    private int transferredStudents; //Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormOfEducation formOfEducation; //Поле не может быть null

    @Column(nullable = false)
    @Min(1)
    private int shouldBeExpelled; //Значение поля должно быть больше 0

    @Column(nullable = false)
    @Min(1)
    private int averageMark; //Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semesterEnum; //Поле не может быть null

    @OneToOne(cascade = CascadeType.ALL)
    private Person groupAdmin; //Поле может быть null
}
