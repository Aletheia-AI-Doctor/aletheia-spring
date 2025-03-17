package dev.aletheia.doctor.services;

import dev.aletheia.doctor.exceptions.DoctorNotFoundException;
import dev.aletheia.doctor.exceptions.NotFoundException;
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
}
