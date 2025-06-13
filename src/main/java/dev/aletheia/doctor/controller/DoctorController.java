package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.dtos.doctors.DoctorPatientsDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.emailservice.AleithiaEmailAuthentication;
import dev.aletheia.doctor.enums.DoctorStates;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DigitalSignService;
import dev.aletheia.doctor.services.ActivityService;
import dev.aletheia.doctor.services.DoctorService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import dev.aletheia.doctor.dtos.doctors.DoctorUpdateDto;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
	private final DigitalSignService digitalSignService;
	private final AleithiaEmailAuthentication emailService;

	public DoctorController(DoctorService doctorService, DigitalSignService digitalSignService, AleithiaEmailAuthentication emailService) {
		this.doctorService = doctorService;
		this.digitalSignService = digitalSignService;
		this.emailService = emailService;
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

	@PostMapping("/appeal/{id}")
	public ResponseEntity<Object> appeal(@PathVariable Long id, @RequestParam(name = "token") String token) throws IOException {
		String tokenConfirm;
		String tokenReject;
		String confirmationUrl = "http://localhost:8080/api/confirm-email/" + id;
		String rejectionUrl = "http://localhost:8080/api/reject-email/" + id;

		try {
			tokenConfirm = digitalSignService.signData(confirmationUrl);
			tokenReject = digitalSignService.signData(rejectionUrl);
		} catch (Exception e) {
			throw new RuntimeException("Error signing data: " + e.getMessage(), e);
		}

		Doctor doctor = doctorService.findOrFail(id);

		confirmationUrl += "?token=" + tokenConfirm;
		rejectionUrl += "?token=" + tokenReject;

		emailService.sendConfirmationRequest(
				doctor.getHospital().getHr_email(),
				doctor.getName(),
				doctor.getSpeciality().toString(),
				doctor.getLicenseNumber(),
				confirmationUrl,
				rejectionUrl
		);
        doctor.setStatus(DoctorStates.PENDING);
        doctorService.save(doctor);

		return ResponseEntity.ok(Map.of(
				"message", "Appeal successful! Please check your email for confirmation instructions.",
				"success", true
		));

	}
    @PutMapping("/update")
public ResponseEntity<Object> updateDoctor(@RequestBody DoctorUpdateDto dto) {
    Doctor doctor = doctorService.getCurrentDoctor();

    // Validate and update email
    if (dto.getEmail() != null && !dto.getEmail().equals(doctor.getEmail())) {
        if (doctorService.isEmailTaken(dto.getEmail(), doctor.getId())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }
        doctor.setEmail(dto.getEmail());
    }

    // Validate and update username
    if (dto.getUsername() != null && !dto.getUsername().equals(doctor.getUsername())) {
        if (doctorService.isUsernameTaken(dto.getUsername(), doctor.getId())) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }
        doctor.setUsername(dto.getUsername());
    }

    if (dto.getName() != null) {
        doctor.setName(dto.getName());
    }

    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
        // Assuming password hashing is handled in save() or needs to be done here
        doctor.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
    }

    if (dto.getBio() != null) {
        doctor.setBio(dto.getBio());
    }

    Doctor updated = doctorService.save(doctor);
    return ResponseEntity.ok(doctorService.convertToDto(updated));
}

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("doctor", new DoctorRegistrationDTO());
        return "register";
    }

    @PostMapping
    public String register(@ModelAttribute("doctor") @Valid DoctorRegistrationDTO doctorDto,
                           BindingResult result,
                           Model model) {

        if (doctorService.emailExists(doctorDto.getEmail())) {
            result.rejectValue("email", "error.doctor", "Email is already in use");
        }

        if (doctorService.usernameExists(doctorDto.getUsername())) {
            result.rejectValue("username", "error.doctor", "Username is already in use");
        }

        if (result.hasErrors()) {
            return "register";
        }

        doctorService.createDoctor(doctorDto);
        return "redirect:/login?success";
    }
}

	
}
