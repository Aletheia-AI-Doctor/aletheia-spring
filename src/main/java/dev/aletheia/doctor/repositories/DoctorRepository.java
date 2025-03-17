package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
//    Optional<Doctor> findByUsername(String username);
}
