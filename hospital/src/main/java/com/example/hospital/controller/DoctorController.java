package com.example.hospital.controller;

import com.example.hospital.model.Chamber;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.dto.DoctorDto;
import com.example.hospital.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hospital")
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    ModelMapper mapper;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDto>> getAllDoctors(@RequestParam Optional<Integer> page,
                                         @RequestParam Optional<String> sortBy){
        try {
            List<Doctor> doctors=doctorRepository.findAll(PageRequest.of(
                    page.orElse(0),
                    5,
                    Sort.Direction.ASC,sortBy.orElse("id")
            )).stream().collect(Collectors.toList());
            if(doctors.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(doctors.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable("id") Long id){
        Optional<Doctor> doctor=doctorRepository.findById(id);

        if(doctor.isPresent()){
            return new ResponseEntity<>(convertToDto(doctor.get()),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/doctors")
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto doctorDto){
        try {
            doctorRepository.save(new Doctor(doctorDto.getName(),doctorDto.getAge()));
            return new ResponseEntity<>(doctorDto,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable("id")Long id,@RequestBody DoctorDto doctorDto){
        Optional<Doctor> doctorData=doctorRepository.findById(id);

        if(doctorData.isPresent()){
            Doctor _doctor=doctorData.get();
            _doctor.setName(doctorDto.getName());
            _doctor.setAge(doctorDto.getAge());
            return new ResponseEntity<>(convertToDto(doctorRepository.save(_doctor)), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<HttpStatus> deleteDoctor(@PathVariable("id") Long id) {
        try {
            doctorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/doctors")
    public ResponseEntity<HttpStatus> deleteAllDoctros() {
        try {
            doctorRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private DoctorDto convertToDto(Doctor doctor){
        DoctorDto doctorDto=mapper.map(doctor,DoctorDto.class);
        return doctorDto;
    }
}
