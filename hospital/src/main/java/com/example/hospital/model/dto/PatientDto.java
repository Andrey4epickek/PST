package com.example.hospital.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PatientDto {
    private String name;
    private Integer age;
    private Long chamberId;
    private Long doctorId;
    private List<Long> medicamentId;
}
