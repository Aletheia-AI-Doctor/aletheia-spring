package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
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

	@GetMapping("/{doctorId}")
	public ResponseEntity<Object> show(@PathVariable Long doctorId, HttpSession session) {
		DoctorDto doctor = doctorService.convertToDto(doctorService.findOrFail(doctorId));
		session.setAttribute("currentDoctor", doctor);
		return ResponseEntity.ok(doctor);
	}
	@GetMapping("/currentUser")
	public DoctorDto getCurrentDoctor(HttpSession session) {
		return (DoctorDto) session.getAttribute("currentDoctor");
	}



	@PostMapping
	public ResponseEntity<Object> create(@RequestBody DoctorRegistrationDTO doctorDTO) {
		return ResponseEntity.ok(
				doctorService.createDoctor(doctorDTO)
		);
	}

}
