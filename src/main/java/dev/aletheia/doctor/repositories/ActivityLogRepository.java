package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
}
// This repository interface extends JpaRepository to provide CRUD operations for the ActivityLog entity.
// It includes a method to find activity logs by doctor ID, ordered by creation date in descending order.   