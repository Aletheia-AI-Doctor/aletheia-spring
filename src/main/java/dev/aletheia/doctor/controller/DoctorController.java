package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.dtos.doctors.DoctorPatientsDto;
import dev.aletheia.doctor.emailservice.EmailSender;
import dev.aletheia.doctor.enums.DoctorStates;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DigitalSignService;
import dev.aletheia.doctor.services.DoctorService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.aletheia.doctor.dtos.doctors.DoctorUpdateDto;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Optional;


@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    private final Validator validator;

    public DoctorController(DoctorService doctorService, Validator validator) {
        this.doctorService = doctorService;
        this.validator = validator;
    }

    @GetMapping("/currentUser")
    public DoctorDto getCurrentDoctor() {
        Doctor doctor = doctorService.getCurrentDoctor();
        return doctorService.convertToDto(doctor);
    }

    @GetMapping("/{doctorId}/show")
    public ResponseEntity<Object> show(@PathVariable Long doctorId) {
        DoctorDto doctor = doctorService.convertToDto(doctorService.findOrFail(doctorId));
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/patientsCount")
    public ResponseEntity<Object> countPatients() {
        Doctor doctor = doctorService.getCurrentDoctor();
        Optional<DoctorPatientsDto> allCounts = doctorService.countDoctorPatients(doctor.getId());
        return ResponseEntity.ok(allCounts);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateDoctor(@RequestBody DoctorUpdateDto dto) {
        Doctor doctor = doctorService.getCurrentDoctor();
        dto.setId(doctor.getId());

        Set<ConstraintViolation<DoctorUpdateDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(null, violations);
        }

        return ResponseEntity.ok(doctorService.convertToDto(doctorService.updateDoctor(doctor, dto)));
    }
}
