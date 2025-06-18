package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.auth.SignInDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.enums.DoctorStates;
import dev.aletheia.doctor.exceptions.InvalidCredentialsException;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DoctorService;
import dev.aletheia.doctor.services.JWTService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class AuthenticationController {

    private final DoctorService doctorService;
    private final JWTService jwtService;

    public AuthenticationController(DoctorService doctorService, JWTService jwtService) {
        this.doctorService = doctorService;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<Object> signIn(@RequestBody SignInDto signInDto) {
        Doctor doctor = doctorService.getByIdentifier(signInDto.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!doctor.checkPassword(signInDto.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (doctor.getStatus() != DoctorStates.CONFIRMED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "message", "Please confirm your email address before logging in",
                    "success", false
            ));
        }

        return ResponseEntity.ok(Map.of(
                        "message", "Login successful",
                        "token", jwtService.generateToken(doctor),
                        "doctor", doctorService.convertToDto(doctor)
                )
        );
    }

    @PostMapping("/api/register")
    public ResponseEntity<Object> create(@RequestBody @Valid DoctorRegistrationDTO doctorDTO) {
        Doctor doctor = doctorService.createDoctor(doctorDTO);

        return ResponseEntity.ok(Map.of(
                "message", "Registration successful! Please check your email for confirmation instructions.",
                "doctor", doctorService.convertToDto(doctor),
                "success", true
        ));
    }
}