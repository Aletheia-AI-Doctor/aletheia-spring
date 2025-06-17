package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

}
