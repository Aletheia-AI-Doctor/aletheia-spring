package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.patient.PatientDto;
import dev.aletheia.doctor.dtos.patient.PatientRegistrationDTO;
import dev.aletheia.doctor.models.Patient;
import dev.aletheia.doctor.repositories.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

@Service
@Transactional
public class PatientService extends CRUDService<Patient, PatientDto> {
   
    @Autowired
    private PatientRepository PatientRepository;

    public Patient createPatient(PatientRegistrationDTO PatientDTO) {
        Patient patient = new Patient();
        patient.setBirthdate(PatientDTO.getBirthdate());
        patient.setSex(PatientDTO.getSex());
        patient.setName(PatientDTO.getName());
        patient.setAddmissionDate(LocalDate.now());
        return save(patient);
    }


    
}
