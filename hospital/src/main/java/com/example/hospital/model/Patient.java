package com.example.hospital.model;

import lombok.*;

import javax.persistence.*;

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

    public Patient() {
    }

    public Patient(String name, Integer age, Chamber chamber, Doctor doctor) {
        this.name = name;
        this.age = age;
        this.chamber = chamber;
        this.doctor = doctor;
    }

}
