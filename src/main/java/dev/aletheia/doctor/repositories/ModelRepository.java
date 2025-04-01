package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findByPath(String path);
}
