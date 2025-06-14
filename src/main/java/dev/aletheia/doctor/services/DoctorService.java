package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.dtos.doctors.DoctorPatientsDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.enums.DoctorSpeciality;
import dev.aletheia.doctor.enums.DoctorStates;
import dev.aletheia.doctor.models.ActivityLog;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Hospital;
import dev.aletheia.doctor.repositories.ActivityLogRepository;
import dev.aletheia.doctor.repositories.DoctorRepository;
import dev.aletheia.doctor.repositories.HospitalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import dev.aletheia.doctor.dtos.doctors.DoctorUpdateDto;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;


@Service
@Transactional
public class DoctorService extends CRUDService<Doctor, DoctorDto> {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    public DoctorRepository getRepository() {
        return doctorRepository;
    }
    public boolean emailExists(String email) {
        return doctorRepository.existsByEmail(email);
    }
    
    public boolean usernameExists(String username) {
        return doctorRepository.existsByUsername(username);
    }
    public boolean isUsernameTaken(String username, Long currentUserId) {
        return doctorRepository.existsByUsernameAndIdNot(username, currentUserId);
    }
    
    public boolean isEmailTaken(String email, Long currentUserId) {
        return doctorRepository.existsByEmailAndIdNot(email, currentUserId);
    }

    protected DoctorService() {super(Doctor.class, DoctorDto.class);}

    public Optional<Doctor> getByIdentifier(String identifier) {
        return doctorRepository.findByEmailOrUsername(identifier);
    }

    public Doctor createDoctor(DoctorRegistrationDTO doctorDTO) {

        if (doctorRepository.existsByEmail(doctorDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
    
        // Check for existing username
        if (doctorRepository.existsByUsername(doctorDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already in use.");
        }

        Doctor doctor = new Doctor();

        doctor.setName(doctorDTO.getName());
        doctor.setUsername(doctorDTO.getUsername());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPassword(doctorDTO.getPassword());

    
        doctor.setStatus(DoctorStates.PENDING);
        doctor.setSpeciality(DoctorSpeciality.valueOf(doctorDTO.getSpeciality()));
        doctor.setLicenseNumber(doctorDTO.getLicenseNumber());
        Hospital hospital = hospitalRepository.findById(doctorDTO.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital not found"));
        doctor.setHospital(hospital);

        return save(doctor);
    }

    public boolean confirmDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid confirmation token"));

        doctor.setStatus(DoctorStates.CONFIRMED);
        save(doctor);

        return true;
    }

    public boolean rejectDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid confirmation token"));

        doctor.setStatus(DoctorStates.REJECTED);
        save(doctor);

        return true;
    }

    public Doctor getCurrentDoctor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Doctor doctor = (Doctor) auth.getPrincipal();

        return findOrFail(doctor.getId());
    }

    public Optional<DoctorPatientsDto> countDoctorPatients(Long doctorId) {
        return doctorRepository.countDoctorPatients(doctorId);
    }
}
