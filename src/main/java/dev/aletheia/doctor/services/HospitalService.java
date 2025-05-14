package dev.aletheia.doctor.services;
import dev.aletheia.doctor.dtos.hospitals.HospitalDto;
import dev.aletheia.doctor.models.Hospital;
import dev.aletheia.doctor.repositories.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class HospitalService extends CRUDService<Hospital, HospitalDto>
{
    @Autowired
    private HospitalRepository hospitalRepository;
    public HospitalRepository getRepository() { return hospitalRepository; }
    protected HospitalService() {super(Hospital.class, HospitalDto.class);}

    public Optional<Hospital> getByHr_emailOrName(String hr_emailOrName) {
        return hospitalRepository.findByHr_emailOrName(hr_emailOrName);
    }
    public Hospital createHospital(HospitalDto hospitalDTO) {
        Hospital hospital = new Hospital();

        hospital.setName(hospitalDTO.getName());
        hospital.setHr_email(hospitalDTO.getHr_email());
        return save(hospital);
    }
}
