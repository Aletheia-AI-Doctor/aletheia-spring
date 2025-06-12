package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.dtos.doctors.DoctorPatientsDto;
import dev.aletheia.doctor.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM doctors d WHERE d.email = ?1 OR d.username = ?1")
    Optional<Doctor> findByEmailOrUsername(String emailOrUsername);
    @Query("SELECT " +
            "new dev.aletheia.doctor.dtos.doctors.DoctorPatientsDto(" +
            "COUNT(p), " +
            "SUM(CASE WHEN p.status = 'PENDING' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status = 'DIAGNOSED' THEN 1 ELSE 0 END)) " +
            "FROM patients p WHERE p.doctor.id = ?1")
    Optional<DoctorPatientsDto> countDoctorPatients(Long doctorId);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
    

}


