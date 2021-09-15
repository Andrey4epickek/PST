package com.example.hospital.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter

@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @OneToOne
    @JoinColumn(name = "chamber_id")
    private Chamber chamber;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToMany
    @JoinTable(name = "patient_medicaments",joinColumns = {@JoinColumn(name = "patient_id",referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "medicament_id",referencedColumnName = "id")})
    private List<Medicament> medicaments;


    public Patient() {
    }

    public Patient(String name, Integer age, Chamber chamber, Doctor doctor) {
        this.name = name;
        this.age = age;
        this.chamber = chamber;
        this.doctor = doctor;
    }

    public Patient(String name, Integer age, Chamber chamber, Doctor doctor, List<Medicament> medicaments) {
        this.name = name;
        this.age = age;
        this.chamber = chamber;
        this.doctor = doctor;
        this.medicaments = medicaments;
    }
}
