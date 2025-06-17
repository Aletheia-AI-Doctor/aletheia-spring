package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.hospitals.HospitalDto;
import dev.aletheia.doctor.models.Hospital;
import dev.aletheia.doctor.services.HospitalService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Object> index() {
        List<Hospital> hospitals = hospitalService.getAll();
        return ResponseEntity.ok(hospitals.stream().map(hospitalService::convertToDto).toList());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> create(@RequestBody @Valid HospitalDto hospitalDto){
        return ResponseEntity.ok(
          hospitalService.createHospital(hospitalDto)
        );
    }
}
