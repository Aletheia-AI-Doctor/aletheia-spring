package dev.aletheia.doctor.controller;

import java.util.List;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.aletheia.doctor.dtos.patient.PatientDto;
import dev.aletheia.doctor.dtos.patient.PatientRegistrationDTO;
import dev.aletheia.doctor.services.PatientService;
import dev.aletheia.doctor.models.Patient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/patients")

public class PatientController {
    private final PatientService patientService;
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    
    @PutMapping("/add")
    public ResponseEntity<Object> create(@RequestBody PatientRegistrationDTO patientDTO) {
        return ResponseEntity.ok(
                patientService.convertToDto(patientService.createPatient(patientDTO))
        );
    }
    
    @GetMapping
    public ResponseEntity<Object> getAllPatient() {
        return ResponseEntity.ok(patientService.getAllDTO());
    }

    @GetMapping("/{patientId}/show")
    public ResponseEntity<Object> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.convertToDto(patientService.findOrFail(patientId)));
    }
}
