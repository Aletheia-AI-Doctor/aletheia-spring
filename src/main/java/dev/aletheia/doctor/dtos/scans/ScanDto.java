package dev.aletheia.doctor.dtos.scans;

import dev.aletheia.doctor.dtos.models.DiagnosisDto;
import dev.aletheia.doctor.dtos.models.ModelDto;
import dev.aletheia.doctor.dtos.patient.PatientDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScanDto {
    private Long id;
    private PatientDto patient;
    private DiagnosisDto modelDiagnosis;
    private DiagnosisDto doctorDiagnosis;
    private String image;
    private String imageResponse;
    private String imageResponseUrl;
    private String imageUrl;
    private ModelDto model;

    public String getImageUrl() {
        return "/api/scans/" + image + "/image";
    }

    public String getImageResponseUrl() {
        return "/api/scans/" + imageResponse + "/image";
    }
}
