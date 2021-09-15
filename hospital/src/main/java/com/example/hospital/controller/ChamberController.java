package com.example.hospital.controller;

import com.example.hospital.model.Chamber;
import com.example.hospital.model.dto.ChamberDto;
import com.example.hospital.repository.ChamberRepository;
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
public class ChamberController {

    @Autowired
    ChamberRepository chamberRepository;
    @Autowired
    ModelMapper mapper;

    @GetMapping("/chambers")
    public ResponseEntity<List<ChamberDto>> getAllChambers(@RequestParam Optional<Integer> page,
                                                           @RequestParam Optional<String> sortBy){
        try {
            List<Chamber> chambers=chamberRepository.findAll(PageRequest.of(
                    page.orElse(0),
                    5,
                    Sort.Direction.ASC,sortBy.orElse("id")
            )).stream().collect(Collectors.toList());
            if(chambers.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(chambers.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/chambers/{id}")
    public ResponseEntity<ChamberDto> getChamberById(@PathVariable("id") Long id){
        Optional<Chamber> chamber=chamberRepository.findById(id);

        if(chamber.isPresent()){
            return new ResponseEntity<>(convertToDto(chamber.get()),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/chambers")
    public ResponseEntity<ChamberDto> createChamber(@RequestBody ChamberDto chamberDto){
        try {
            chamberRepository.save(new Chamber(chamberDto.getNumber()));
            return new ResponseEntity<>(chamberDto,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/chambers/{id}")
    public ResponseEntity<ChamberDto> updateChamber(@PathVariable("id")Long id,@RequestBody ChamberDto chamberDto){
        Optional<Chamber> chamberData=chamberRepository.findById(id);

        if(chamberData.isPresent()){
            Chamber _chamber=chamberData.get();
            _chamber.setNumber(chamberDto.getNumber());
            return new ResponseEntity<>(convertToDto(chamberRepository.save(_chamber)), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/chambers/{id}")
    public ResponseEntity<HttpStatus> deleteChamber(@PathVariable("id") Long id) {
        try {
            chamberRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/chambers")
    public ResponseEntity<HttpStatus> deleteAllChambers() {
        try {
            chamberRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private ChamberDto convertToDto(Chamber chamber){
        ChamberDto chamberDto=mapper.map(chamber,ChamberDto.class);
        return chamberDto;
    }
}
