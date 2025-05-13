package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.scans.SaveScanDto;
import dev.aletheia.doctor.dtos.scans.ScanDto;
import dev.aletheia.doctor.models.*;
import dev.aletheia.doctor.repositories.ScanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private DoctorService doctorService;

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
        scan.setDoctor(doctorService.getCurrentDoctor());

        return scanRepository.save(scan);
    }

    public List<ScanDto> getAllForDoctor(Doctor doctor) {
        List<Scan> scans = scanRepository.findAllByDoctor(doctor);
        return scans.stream().map(this::convertToDto).toList();
    }
}
