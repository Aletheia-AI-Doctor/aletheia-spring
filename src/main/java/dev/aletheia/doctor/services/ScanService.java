package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.scans.SaveScanDto;
import dev.aletheia.doctor.dtos.scans.ScanDto;
import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.models.Model;
import dev.aletheia.doctor.models.Patient;
import dev.aletheia.doctor.models.Scan;
import dev.aletheia.doctor.repositories.ScanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScanService extends CRUDService<Scan, ScanDto> {
    @Autowired
    private ScanRepository scanRepository;
    @Autowired
    private ModelService modelService;
    @Autowired
    private DiagnosisService diagnosisService;
    @Autowired
    private PatientService patientService;

    protected ScanService() {super(Scan.class, ScanDto.class);}

    public ScanRepository getRepository() {return scanRepository;}

    public Scan create(SaveScanDto saveScanDto) {
        Scan scan = new Scan();

        Model model = modelService.getBySlug(saveScanDto.getModel());
        Diagnosis modelDiagnosis = diagnosisService.getByNameAndModel(model, saveScanDto.getModelDiagnosis());

        scan.setModelDiagnosis(modelDiagnosis);
        if(saveScanDto.getPatientId() != null) {
            Patient patient = patientService.findOrFail(saveScanDto.getPatientId());
            scan.setPatient(patient);
        }
        scan.setImage(saveScanDto.getImagePath());

        return scanRepository.save(scan);
    }
}
