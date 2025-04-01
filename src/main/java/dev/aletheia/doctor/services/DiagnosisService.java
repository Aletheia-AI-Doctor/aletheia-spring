package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.models.DiagnosisDto;
import dev.aletheia.doctor.exceptions.NotFoundException;
import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.repositories.DiagnosisRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DiagnosisService extends CRUDService<Diagnosis, DiagnosisDto> {
    @Autowired
    private DiagnosisRepository diagnosisRepository;

    public DiagnosisRepository getRepository() { return diagnosisRepository; }

    protected DiagnosisService() {super(Diagnosis.class, DiagnosisDto.class);}

    public Diagnosis getByName(String name) {
        return diagnosisRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Diagnosis not found"));
    }
}
