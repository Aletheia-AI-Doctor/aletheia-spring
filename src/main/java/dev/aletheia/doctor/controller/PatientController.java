package dev.aletheia.doctor.controller;

import java.util.List;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.aletheia.doctor.dtos.patient.PatientDto;
import dev.aletheia.doctor.dtos.patient.PatientRegistrationDTO;
import dev.aletheia.doctor.services.PatientService;
import dev.aletheia.doctor.models.Patient;


@RestController
@RequestMapping("api/patient")

public class PatientController {
    private final PatientService patientService;
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GetMapping("/{patientId}")
    public ResponseEntity<Object> show(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.convertToDto(patientService.findOrFail(patientId)));
    }
    
    @PostMapping("/add")
    public ResponseEntity<Object> create(@RequestBody PatientRegistrationDTO patientDTO) {
        return ResponseEntity.ok(
                patientService.createPatient(patientDTO)
        );
    }
    
    @GetMapping

    public ResponseEntity<Object> getAllPatient(){
        return ResponseEntity.ok(patientService.getAll());
    }
    

    
    


    
}
