package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.PaginationDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    public ResponseEntity<Object> getAllPatient(@RequestParam @Nullable Integer page) {
        return ResponseEntity.ok(
                new PaginationDTO<>(
                        patientService.getAllPaginated(PageRequest.of(
                                page == null ? 0 : page, 10
                        ))
                )
        );
    }

    @GetMapping("/{patientId}/show")
    public ResponseEntity<Object> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.convertToDto(patientService.findOrFail(patientId)));
    }

    @PutMapping("/{patientId}/update")
    public ResponseEntity<Object> updatepatientstatus(@PathVariable Long patientId, @RequestBody PatientDto patientDTO) {
        Patient patient = patientService.findOrFail(patientId);

        patientService.updatePatientStatus(patientService.findOrFail(patientId), PatientStatus.fromString(patientDTO.getStatus()));

        return ResponseEntity.ok(patientService.convertToDto(patient));
    }
}
