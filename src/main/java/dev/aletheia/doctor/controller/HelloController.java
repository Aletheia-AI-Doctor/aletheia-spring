package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.services.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private final DoctorService doctorService;

	public HelloController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	@GetMapping("/{doctorId}")
	public ResponseEntity<Object> index(@PathVariable Long doctorId) {
		return ResponseEntity.ok(doctorService.getDoctor(doctorId));
	}

}
