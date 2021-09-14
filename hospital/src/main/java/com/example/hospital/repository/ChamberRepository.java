package com.example.hospital.repository;

import com.example.hospital.model.Chamber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamberRepository extends JpaRepository<Chamber,Long> {
}
