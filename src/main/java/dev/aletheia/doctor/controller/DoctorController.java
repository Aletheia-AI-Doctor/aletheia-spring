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
import org.springframework.web.bind.annotation.*;
import dev.aletheia.doctor.dtos.doctors.DoctorUpdateDto;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<Object> index() {
        return ResponseEntity.ok(doctorService.find(1L));
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

        if (dto.getName() != null) {
            doctor.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            doctor.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            doctor.setPassword(dto.getPassword());
        }

        Doctor updated = doctorService.save(doctor);
        return ResponseEntity.ok(doctorService.convertToDto(updated));
    }
}
