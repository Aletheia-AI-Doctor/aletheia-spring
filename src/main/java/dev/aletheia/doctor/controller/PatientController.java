package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.PaginationDTO;
import dev.aletheia.doctor.dtos.patient.PatientStatusDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

import dev.aletheia.doctor.dtos.patient.PatientDto;
import dev.aletheia.doctor.dtos.patient.PatientRegistrationDTO;
import dev.aletheia.doctor.enums.PatientStatus;
import dev.aletheia.doctor.services.PatientService;
import jakarta.validation.Valid;
import dev.aletheia.doctor.models.Patient;


@RestController
@RequestMapping("api/patients")

public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid PatientRegistrationDTO patientDto) {
        Patient patient = patientService.createPatient(patientDto);

        return ResponseEntity.ok(Map.of(
                "message", "Patient created successfully",
                "patient", patientService.convertToDto(patient)
        ));
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

    @GetMapping("/{patientId}")
    public ResponseEntity<Object> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.convertToDto(patientService.findOrFail(patientId)));
    }

    @PatchMapping("/{patientId}/status")
    public ResponseEntity<Object> updatePatientStatus(@PathVariable Long patientId, @RequestBody PatientStatusDto patientDTO) {
        Patient patient = patientService.findOrFail(patientId);

        patientService.updatePatientStatus(patientService.findOrFail(patientId), PatientStatus.fromString(patientDTO.getStatus()));

        return ResponseEntity.ok(patientService.convertToDto(patient));
    }
}
