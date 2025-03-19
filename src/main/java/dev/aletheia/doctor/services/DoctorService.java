package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.exceptions.DoctorNotFoundException;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.repositories.DoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    private DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
    }

    public Doctor createDoctor(DoctorRegistrationDTO doctorDTO) {
        Doctor doctor = new Doctor();

        doctor.setName(doctorDTO.getName());
        doctor.setUsername(doctorDTO.getUsername());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPassword(doctorDTO.getPassword());
        doctor.setBio(doctorDTO.getBio());
        doctor.setSpeciality(doctorDTO.getSpeciality());

        return save(doctor);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
}
