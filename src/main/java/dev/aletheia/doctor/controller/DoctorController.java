package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.enums.DoctorSpeciality;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

	private final DoctorService doctorService;

	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	@GetMapping
	public ResponseEntity<Object> index() {
		return ResponseEntity.ok(doctorService.getDoctor(1L));
	}

	@GetMapping("/{doctorId}")
	public ResponseEntity<Object> show(@PathVariable Long doctorId) {
		return ResponseEntity.ok(doctorService.getDoctor(doctorId));
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody DoctorRegistrationDTO doctorDTO) {
		return ResponseEntity.ok(
				doctorService.createDoctor(doctorDTO)
		);
	}

}
