package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    Optional<Diagnosis> findByName(String name);
}
