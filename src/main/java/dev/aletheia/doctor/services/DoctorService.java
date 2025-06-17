package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.dtos.doctors.DoctorPatientsDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.dtos.doctors.DoctorUpdateDto;
import dev.aletheia.doctor.emailservice.EmailQueueManager;
import dev.aletheia.doctor.enums.DoctorSpeciality;
import dev.aletheia.doctor.enums.DoctorStates;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Hospital;
import dev.aletheia.doctor.repositories.DoctorRepository;
import dev.aletheia.doctor.repositories.HospitalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
public class DoctorService extends CRUDService<Doctor, DoctorDto> {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private EmailQueueManager emailQueueManager;

    public DoctorRepository getRepository() {
        return doctorRepository;
    }

    protected DoctorService() {super(Doctor.class, DoctorDto.class);}

    public Optional<Doctor> getByIdentifier(String identifier) {
        return doctorRepository.findByEmailOrUsername(identifier);
    }

    public Doctor createDoctor(DoctorRegistrationDTO doctorDTO) {
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

        doctor = save(doctor);

        emailQueueManager.queueConfirmationRequest(doctor.getId());

        return doctor;
    }

    public Doctor updateDoctor(Doctor doctor, DoctorUpdateDto dto) {
        dto.setId(doctor.getId());

        if (dto.getEmail() != null) {
            doctor.setEmail(dto.getEmail());
        }

        if (dto.getUsername() != null) {
            doctor.setUsername(dto.getUsername());
        }

        if (dto.getName() != null) {
            doctor.setName(dto.getName());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            doctor.setPassword(dto.getPassword());
        }

        if (dto.getBio() != null) {
            doctor.setBio(dto.getBio());
        }

        return save(doctor);
    }

    public void confirmDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid confirmation token"));

        doctor.setStatus(DoctorStates.CONFIRMED);
        save(doctor);
    }

    public void rejectDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid confirmation token"));

        doctor.setStatus(DoctorStates.REJECTED);
        save(doctor);
    }

    public Doctor getCurrentDoctor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Doctor) auth.getPrincipal();
    }

    public Optional<DoctorPatientsDto> countDoctorPatients(Long doctorId) {
        return doctorRepository.countDoctorPatients(doctorId);
    }
}
