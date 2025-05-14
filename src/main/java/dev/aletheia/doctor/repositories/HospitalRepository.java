package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    @Query("SELECT h FROM hospitals h WHERE h.hr_email =?1 OR h.name= ?2")
    Optional<Hospital> findByHr_emailOrName(String hr_emailOrName);

}
