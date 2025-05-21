package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findAllByDoctor(Doctor doctor, Pageable pageable);
}
