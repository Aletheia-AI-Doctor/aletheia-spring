package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.models.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    Optional<Diagnosis> findByName(String name);
    Optional<Diagnosis> findByNameAndModel(String name, Model model);
    List<Diagnosis> findAllByModel(Model model);;
}
