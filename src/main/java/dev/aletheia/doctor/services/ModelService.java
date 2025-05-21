package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.models.DiagnosisDto;
import dev.aletheia.doctor.dtos.models.ModelDto;
import dev.aletheia.doctor.exceptions.NotFoundException;
import dev.aletheia.doctor.helpers.Response;
import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Model;
import dev.aletheia.doctor.repositories.ModelRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ModelService extends CRUDService<Model, ModelDto> {
    private final ModelRepository modelRepository;
    private final HttpService httpService;
    private final DiagnosisService diagnosisService;
    private final FileService fileService;
    @Autowired
    private ActivityService activityService;


    public ModelRepository getRepository() {
        return modelRepository;
    }

    protected ModelService(ModelRepository modelRepository, HttpService httpService, DiagnosisService diagnosisService, FileService fileService) {
        super(Model.class, ModelDto.class);
        this.modelRepository = modelRepository;
        this.httpService = httpService;
        this.diagnosisService = diagnosisService;
        this.fileService = fileService;

    }

    @Autowired
    private DoctorService doctorService;

    public Model getBySlug(String slug) {
        return modelRepository.findByPath(slug)
                .orElseThrow(() -> new NotFoundException("Model not found"));
    }

    public DiagnosisDto predict(Model model, MultipartFile image) {
        String imagePath = fileService.saveFile(image);
        activityService.log(
                "Add Scan",
                "Uploaded scan for model: " + model.getPath()
        );

        Response response = httpService.get("/" + model.getPath() + "?image_path=" + imagePath);

        if (response.isSuccessful()) {
            DiagnosisDto diagnosis = diagnosisService.convertToDto(diagnosisService.getByName(response.getBody()));

            diagnosis.setImagePath(imagePath);

            return diagnosis;
        }

        DiagnosisDto diagnosisDto = diagnosisService.convertToDto(new Diagnosis("No Result"));

        diagnosisDto.setImagePath(imagePath);

        return diagnosisDto;
    }
}
