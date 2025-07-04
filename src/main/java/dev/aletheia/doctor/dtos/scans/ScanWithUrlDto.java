package dev.aletheia.doctor.dtos.scans;

import dev.aletheia.doctor.dtos.models.DiagnosisDto;
import dev.aletheia.doctor.dtos.models.ModelDto;
import dev.aletheia.doctor.dtos.patient.PatientDto;
import dev.aletheia.doctor.models.Scan;
import dev.aletheia.doctor.services.DigitalSignService;
import lombok.Getter;
import lombok.Setter;

import java.security.GeneralSecurityException;

@Getter
@Setter
public class ScanWithUrlDto {
    private Long id;
    private PatientDto patient;
    private DiagnosisDto modelDiagnosis;
    private DiagnosisDto doctorDiagnosis;
    private String image;
    private String imageResponse;
    private String imageResponseUrl;
    private String imageUrl;
    private ModelDto model;

    private DigitalSignService digitalSignService;

    public ScanWithUrlDto(DigitalSignService digitalSignService) {
        this.digitalSignService = digitalSignService;
    }

    public void populateFromScan(ScanDto scan) {
        this.id = scan.getId();
        this.patient = scan.getPatient();
        this.modelDiagnosis = scan.getModelDiagnosis();
        this.doctorDiagnosis = scan.getDoctorDiagnosis();
        this.image = scan.getImage();
        this.imageResponse = scan.getImageResponse();
        this.model = scan.getModel();
    }
    public String getImageUrl() throws GeneralSecurityException {
        return this.digitalSignService.getSignedUrl("/api/scans/" + image + "/image");
    }

    public String getImageResponseUrl() throws GeneralSecurityException {
        return imageResponse == null || imageResponse.isBlank() ? this.digitalSignService.getSignedUrl("/api/scans/" + imageResponse + "/image") : "";
    }
}
