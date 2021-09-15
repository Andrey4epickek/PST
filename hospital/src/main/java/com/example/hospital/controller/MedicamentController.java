package com.example.hospital.controller;

import com.example.hospital.model.Medicament;
import com.example.hospital.model.dto.MedicamentDto;
import com.example.hospital.repository.MedicamentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MedicamentController {

    @Autowired
    MedicamentRepository medicamentRepository;

    @Autowired
    ModelMapper mapper;

    @GetMapping("/medicaments")
    public ResponseEntity<List<MedicamentDto>> getAllMedicaments(@RequestParam Optional<Integer> page,
                                                         @RequestParam Optional<String> sortBy){
        try {
            List<Medicament> medicaments=medicamentRepository.findAll(PageRequest.of(
                    page.orElse(0),
                    5,
                    Sort.Direction.ASC,sortBy.orElse("id")
            )).stream().collect(Collectors.toList());
            if(medicaments.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(medicaments.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/medicaments/{id}")
    public ResponseEntity<MedicamentDto> getMedicamentById(@PathVariable("id") Long id){
        Optional<Medicament> medicament=medicamentRepository.findById(id);

        if(medicament.isPresent()){
            return new ResponseEntity<>(convertToDto(medicament.get()),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/medicaments")
    public ResponseEntity<MedicamentDto> createMedicament(@RequestBody MedicamentDto medicamentDto){
        try {
            medicamentRepository.save(new Medicament(medicamentDto.getName()));
            return new ResponseEntity<>(medicamentDto,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/medicaments/{id}")
    public ResponseEntity<MedicamentDto> updateMedicament(@PathVariable("id")Long id,@RequestBody MedicamentDto medicamentDto){
        Optional<Medicament> medicamentData=medicamentRepository.findById(id);

        if(medicamentData.isPresent()){
            Medicament _medicament=medicamentData.get();
            _medicament.setName(medicamentDto.getName());
            return new ResponseEntity<>(convertToDto(medicamentRepository.save(_medicament)), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/medicaments/{id}")
    public ResponseEntity<HttpStatus> deleteMedicament(@PathVariable("id") Long id) {
        try {
            medicamentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/medicaments")
    public ResponseEntity<HttpStatus> deleteAllMedicaments() {
        try {
            medicamentRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private MedicamentDto convertToDto(Medicament medicament){
        MedicamentDto medicamentDto=mapper.map(medicament,MedicamentDto.class);
        return medicamentDto;
    }
}
