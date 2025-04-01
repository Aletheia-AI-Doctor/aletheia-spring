package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.models.ModelDto;
import dev.aletheia.doctor.exceptions.NotFoundException;
import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.models.Model;
import dev.aletheia.doctor.repositories.ModelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ModelService extends CRUDService<Model, ModelDto> {
    @Autowired
    private ModelRepository modelRepository;

    public ModelRepository getRepository() { return modelRepository; }

    protected ModelService() {super(Model.class, ModelDto.class);}

    public Model getBySlug(String slug) {
        return modelRepository.findByPath(slug)
                .orElseThrow(() -> new NotFoundException("Model not found"));
    }

    public Diagnosis predict(Model model, MultipartFile image) {
        return new Diagnosis("Non Demented");
    }
}
