package dev.aletheia.doctor.autoload;

import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.models.Model;
import dev.aletheia.doctor.services.ModelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ModelLoader implements CommandLineRunner {

    private final ModelService modelService;

    ModelLoader(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(modelService.getRepository().count() > 0) {
            System.out.println("Models already loaded");
            return;
        }

        Model mri = new Model("MRI", "mri");
        mri.addDiagnosis(new Diagnosis("Non Demented"));
        mri.addDiagnosis(new Diagnosis("Mild Dementia"));
        mri.addDiagnosis(new Diagnosis("Very Mild Dementia"));
        mri.addDiagnosis(new Diagnosis("Very Severe Dementia"));
        modelService.create(mri);

        Model chestXray = new Model("Chest Xray", "chest-xray");
        chestXray.addDiagnosis(new Diagnosis("Normal"));
        chestXray.addDiagnosis(new Diagnosis("Pneumonia"));
        chestXray.addDiagnosis(new Diagnosis("Tuberculosis"));
        modelService.create(chestXray);

        Model breastCancer = new Model("Breast Cancer", "breast-cancer");
        breastCancer.addDiagnosis(new Diagnosis("Cancer"));
        breastCancer.addDiagnosis(new Diagnosis("No Cancer"));
        modelService.create(breastCancer);
    }
}
