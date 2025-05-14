package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.auth.SignInDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.exceptions.InvalidCredentialsException;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DoctorService;
import dev.aletheia.doctor.services.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

        return ResponseEntity.ok(Map.of(
                        "message", "Login successful",
                        "token", jwtService.generateToken(doctor),
                        "doctor", doctorService.convertToDto(doctor)
                )
        );
    }

    @PostMapping("/api/register")
    public ResponseEntity<Object> create(@RequestBody DoctorRegistrationDTO doctorDTO) {
        System.out.println("Doctor DTO: " + doctorDTO);
        Doctor doctor = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(
                doctorService.convertToDto(doctor)
        );
    }
}
