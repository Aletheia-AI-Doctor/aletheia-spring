package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.dtos.doctors.DoctorPatientsDto;
import dev.aletheia.doctor.dtos.doctors.DoctorRegistrationDTO;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.repositories.DoctorRepository;
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

    public DoctorRepository getRepository() { return doctorRepository; }

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
        doctor.setBio(doctorDTO.getBio());
        doctor.setSpeciality(doctorDTO.getSpeciality());

        return save(doctor);
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
