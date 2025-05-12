package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.dtos.doctors.DoctorPatientsDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DoctorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.hibernate.annotations.Cache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.aletheia.doctor.dtos.doctors.DoctorUpdateDto;

import java.util.Optional;

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
		System.out.println(doctor);
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

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody DoctorRegistrationDTO doctorDTO) {
		return ResponseEntity.ok(
				doctorService.createDoctor(doctorDTO)
		);
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

	@GetMapping("/activity-log")
	public ResponseEntity<?> getActivityLog() {
    	Doctor doctor = doctorService.getCurrentDoctor();
    	return ResponseEntity.ok(doctorService.getActivityLogs(doctor.getId()));
}


}
