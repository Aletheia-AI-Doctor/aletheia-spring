package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.patient.PatientDto;
import dev.aletheia.doctor.dtos.patient.PatientRegistrationDTO;
import dev.aletheia.doctor.enums.Gender;
import dev.aletheia.doctor.enums.PatientStatus;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Patient;
import dev.aletheia.doctor.repositories.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class PatientService extends CRUDService<Patient, PatientDto> {

    @Autowired
    private DoctorService doctorService;

    protected PatientService() {
        super(Patient.class, PatientDto.class);
    }
    @Autowired
    private PatientRepository patientRepository;
    
    public PatientRepository getRepository() {
        return patientRepository;
    }

    public Page<PatientDto> getAllPaginated(Pageable pageable) {
        Doctor doctor = doctorService.getCurrentDoctor();

        return patientRepository
                .findAllByDoctor(doctor, pageable)
                .map(this::convertToDto);
    }

    public Patient createPatient(PatientRegistrationDTO PatientDTO) {
        Patient patient = new Patient();
        patient.setBirthdate(PatientDTO.getBirthdate());
        patient.setSex(Gender.fromString(PatientDTO.getSex()));
        patient.setName(PatientDTO.getName());
        patient.setAdmissionDate(LocalDate.now());
        patient.setStatus(PatientStatus.PENDING);
        patient.setDoctor(doctorService.getCurrentDoctor());
        return save(patient);
    }
}
