package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<Scan, Long> {

}
