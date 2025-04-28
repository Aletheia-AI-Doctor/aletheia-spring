package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM doctors d WHERE d.email = ?1 OR d.username = ?1")
    Optional<Doctor> findByEmailOrUsername(String emailOrUsername);
}
