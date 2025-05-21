package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
}