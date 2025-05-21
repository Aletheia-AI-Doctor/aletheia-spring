package dev.aletheia.doctor.dtos.scans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveScanDto {
    private Long patientId;
    private String modelDiagnosis;
    private String doctorDiagnosis;
    private String imagePath;
    private String model;
}
