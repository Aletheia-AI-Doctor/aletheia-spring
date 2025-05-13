package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Scan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScanRepository extends JpaRepository<Scan, Long> {

    @Query("SELECT s FROM scans s WHERE s.doctor = ?1")
    List<Scan> findAllByDoctor(Doctor doctor);
}
