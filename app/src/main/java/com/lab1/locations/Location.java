package com.lab1.locations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Float x; //Поле не может быть null

    @Column(nullable = false)
    private Float y; //Поле не может быть null

    @Column(nullable = false)
    private Double z; //Поле не может быть null

    @Column(length = 694)
    private String name; //Длина строки не должна быть больше 694, Поле не может быть null
}
