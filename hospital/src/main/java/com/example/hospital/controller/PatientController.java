package com.example.hospital.controller;

import com.example.hospital.model.Medicament;
import com.example.hospital.model.Patient;
import com.example.hospital.model.dto.PatientDto;
import com.example.hospital.repository.ChamberRepository;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.MedicamentRepository;
import com.example.hospital.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hospital")
public class PatientController {

    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ChamberRepository chamberRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    MedicamentRepository medicamentRepository;
    @Autowired
    ModelMapper mapper;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>>getAllPatients(@RequestParam Optional<Integer> page,
                                                          @RequestParam Optional<String> sortBy){
        try {
            List<Patient> patients=patientRepository.findAll(PageRequest.of(
                    page.orElse(0),
                    5,
                    Sort.Direction.ASC,sortBy.orElse("id")
            )).stream().collect(Collectors.toList());
            if(patients.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(patients.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable("id") Long id){
        Optional<Patient> patient=patientRepository.findById(id);

        if(patient.isPresent()){
            return new ResponseEntity<>(convertToDto(patient.get()),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/patients")
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto patientDto){
        try {

            patientRepository.save(new Patient(patientDto.getName(),patientDto.getAge(),chamberRepository.getById(patientDto.getChamberId()), doctorRepository.getById(patientDto.getDoctorId()),medicamentRepository.findAllById(patientDto.getMedicamentId())));
            return new ResponseEntity<>(patientDto,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable("id")Long id,@RequestBody PatientDto patientDto){
        Optional<Patient> patientData=patientRepository.findById(id);

        if(patientData.isPresent()){
            Patient _patient=patientData.get();
            _patient.setName(patientDto.getName());
            _patient.setAge(patientDto.getAge());
            _patient.setChamber(chamberRepository.getById(patientDto.getChamberId()));
            _patient.setDoctor(doctorRepository.getById(patientDto.getDoctorId()));
            return new ResponseEntity<>(convertToDto(patientRepository.save(_patient)), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/patietns/{id}")
    public ResponseEntity<HttpStatus> deletePatient(@PathVariable("id") Long id) {
        try {
            patientRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/patietns")
    public ResponseEntity<HttpStatus> deleteAllPatients() {
        try {
            patientRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private PatientDto convertToDto(Patient patient){
        PatientDto patientDto=new PatientDto();
        patientDto.setName(patient.getName());
        patientDto.setAge(patient.getAge());
        patientDto.setChamberId(patient.getChamber().getId());
        patientDto.setDoctorId(patient.getDoctor().getId());

        List<Medicament> medicaments=patient.getMedicaments();
        List<Long> medicamentsId=new ArrayList<>();
        for (Medicament medicament:medicaments){
            medicamentsId.add(medicament.getId());
        }

        patientDto.setMedicamentId(medicamentsId);
        return patientDto;
    }

}


