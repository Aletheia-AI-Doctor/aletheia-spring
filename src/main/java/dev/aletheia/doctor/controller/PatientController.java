package dev.aletheia.doctor.controller;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.aletheia.doctor.dtos.patient.PatientDto;
import dev.aletheia.doctor.dtos.patient.PatientRegistrationDTO;
import dev.aletheia.doctor.services.PatientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/patient")

public class PatientController {
    private final PatientService patientService;
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @RequestMapping
    public ResponseEntity<Object> index() {
        return ResponseEntity.ok(patientService.find(1L));
    }
    
    @RequestMapping("/{patientId}")
    public ResponseEntity<Object> show(Long patientId) {
        return ResponseEntity.ok(patientService.convertToDto(patientService.findOrFail(patientId)));
    }
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody PatientRegistrationDTO patientDTO) {
        return ResponseEntity.ok(
                patientService.createPatient(patientDTO)
        );
    }
    
    @RequestMapping
    public ResponseEntity<Object> getAllPatient(){
        return ResponseEntity.ok(patientService.getAll());
    }
    

    
    


    
}
