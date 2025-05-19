package dev.aletheia.doctor.controller;

import java.util.List;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.aletheia.doctor.dtos.patient.PatientDto;
import dev.aletheia.doctor.dtos.patient.PatientRegistrationDTO;
import dev.aletheia.doctor.enums.PatientStatus;
import dev.aletheia.doctor.services.PatientService;
import dev.aletheia.doctor.models.Patient;




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

    @PutMapping("/{patientId}/update")
    public ResponseEntity<Object> updatepatientstatus(@PathVariable Long patientId, @RequestBody PatientDto patientDTO) {
        Patient patient = patientService.findOrFail(patientId);
        patient.setStatus(PatientStatus.fromString(patientDTO.getStatus()));
        patientService.save(patient);
        return ResponseEntity.ok(patientService.convertToDto(patient));
    }
}
