package com.example.hospital.model.dto;

import lombok.Data;

@Data
public class PatientDto {
    private String name;
    private Integer age;
    private Long chamberId;
    private Long doctorId;
}
