package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.hospitals.HospitalDto;
import dev.aletheia.doctor.models.Hospital;
import dev.aletheia.doctor.services.HospitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")

public class HospitalController {
    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }
    @GetMapping
    public ResponseEntity<Object> index(){return ResponseEntity.ok(hospitalService.find(1L));}

    @PostMapping("/register")
    public ResponseEntity<Object> create(@RequestBody HospitalDto hospitalDto){
        return ResponseEntity.ok(
          hospitalService.createHospital(hospitalDto)
        );
    }

    @GetMapping("/{hr_emailOrName}/show")
    public ResponseEntity<Object> show(@PathVariable Long hospitalId){
        HospitalDto hospital = hospitalService.convertToDto(hospitalService.findOrFail(hospitalId));
        return ResponseEntity.ok(hospital);

    }

    @GetMapping("/hospitals")
    public ResponseEntity<Object> getHospitals(){
        List<Hospital> hospitals = hospitalService.getAll();
        return ResponseEntity.ok(hospitals.stream().map(hospitalService::convertToDto).toList());
    }
}
