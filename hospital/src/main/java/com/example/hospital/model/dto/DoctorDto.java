package com.example.hospital.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DoctorDto {
    private String name;
    private Integer age;
    private List<PatientDto> patients;
}
