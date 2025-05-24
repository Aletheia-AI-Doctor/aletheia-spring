package dev.aletheia.doctor.dtos.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DiagnosisDto {
    private String id;
    private String name;
    private String imagePath;
    private String imageResponsePath;
}
