package com.example.hospital.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients;

    public Doctor() {
    }

    public Doctor(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

}
