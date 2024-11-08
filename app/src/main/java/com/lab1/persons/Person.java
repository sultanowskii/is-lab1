package com.lab1.persons;

import com.lab1.locations.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import com.lab1.common.Owned;

@Entity
@Getter
@Setter
public class Person extends Owned implements com.lab1.common.Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color", nullable = false)
    private Color eyeColor;

    @Column(name = "hair_color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Min(1)
    @Column(name = "height", nullable = false)
    private Integer height;
}
