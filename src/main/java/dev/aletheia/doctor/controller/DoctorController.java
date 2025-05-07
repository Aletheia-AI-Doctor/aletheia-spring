package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.annotations.Cache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

	private final DoctorService doctorService;

	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
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

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody DoctorRegistrationDTO doctorDTO) {
		return ResponseEntity.ok(
				doctorService.createDoctor(doctorDTO)
		);
	}

}
