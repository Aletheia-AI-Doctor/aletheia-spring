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
    private PatientRepository PatientRepository;

    public PatientRepository getRepository() {
        return PatientRepository;
    }

    public Optional<Patient> getByIdentifier(String identifier) {
        return PatientRepository.findById(Long.valueOf(identifier));
    }

    public Patient createPatient(PatientRegistrationDTO PatientDTO) {
        Doctor doctor = doctorService.getCurrentDoctor();

        Patient patient = new Patient();
        patient.setBirthdate(PatientDTO.getBirthdate());
        patient.setSex(Gender.fromString(PatientDTO.getSex()));
        patient.setName(PatientDTO.getName());
        patient.setAddmissionDate(LocalDate.now());
        patient.setStatus(PatientStatus.fromString(PatientDTO.getStatus()));
        System.out.println("Patient DTO in create patient: " + PatientDTO);
        patient.setDoctor(doctor);

        Patient savedPatient = save(patient);

        doctorService.logActivity(
                doctor,
                "Add Patient",
                "Added patient: " + patient.getName() + " with ID: " + savedPatient.getId() + " to the system.");

        return save(patient);

    }

    public Patient updatePatientStatus(Long patientId, String newStatus) {
        Doctor doctor = doctorService.getCurrentDoctor();
        Patient patient = getRepository().findById(patientId).get();

        PatientStatus currentStatus = patient.getStatus();
        PatientStatus updatedStatus = PatientStatus.fromString(newStatus);

        if (currentStatus != updatedStatus) {
            patient.setStatus(updatedStatus);

            if (currentStatus == PatientStatus.PENDING && updatedStatus == PatientStatus.DIAGNOSED) {
                doctorService.logActivity(
                        doctor,
                        "Diagnose Patient",
                        "Changed status to diagnosed for: " + patient.getName() + " with ID: " + patient.getId());
            }

            return save(patient);
        }

        return patient;
    }

}
