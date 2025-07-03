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

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private DigitalSignService digitalSignService;

    @Value("${spring.application.url}")
    private String appUrl;

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

    public String getValue(String input, String key) {
        // Build the search pattern e.g., "image_path":"
        String search = '"' + key + "\":\"";
        int start = input.indexOf(search);
        if (start == -1) {
            return null;
        }
        // Move past the pattern to start of the value
        start += search.length();
        // Find the closing double quote
        int end = input.indexOf('"', start);
        if (end == -1) {
            return null;
        }
        // Extract and return the value
        return input.substring(start, end);
    }

    public DiagnosisDto predict(Model model, MultipartFile image) {
        String imagePath = fileService.saveFile(image);
        activityService.log(
                "Add Scan",
                "Uploaded scan for model: " + model.getPath()

        );
       
        Response response = httpService.get("/" + model.getPath() + "?image_path=" + imagePath);

        if (response.isSuccessful()) {
            String jsonResponse = response.getBody();

            DiagnosisDto diagnosis = diagnosisService.convertToDto(
                    diagnosisService.getByName(getValue(jsonResponse, "name"))
            );
            try {
                String url = digitalSignService.getSignedUrl(
                        "/api/scans/" + getValue(jsonResponse, "image_path") + "/image"
                );

                diagnosis.setImageResponseUrl(appUrl + url);
            } catch (Exception ignored) {}

            diagnosis.setImagePath(imagePath);

            return diagnosis;
        }

        DiagnosisDto diagnosisDto = diagnosisService.convertToDto(new Diagnosis("No Result"));

        diagnosisDto.setImagePath(imagePath);

        return diagnosisDto;
    }
}
