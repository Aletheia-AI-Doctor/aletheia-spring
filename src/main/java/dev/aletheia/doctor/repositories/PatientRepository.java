package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientRepository extends JpaRepository<Patient, Long> {

}
