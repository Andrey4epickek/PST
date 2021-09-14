package com.example.hospital.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "chambers")
public class Chamber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @OneToOne(mappedBy = "chamber")
    private Patient patient;

    public Chamber() {
    }

    public Chamber(Integer number) {
        this.number = number;
    }

}
