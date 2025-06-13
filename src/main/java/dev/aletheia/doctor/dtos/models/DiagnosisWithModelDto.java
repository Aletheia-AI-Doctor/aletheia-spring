package dev.aletheia.doctor.dtos.models;

import dev.aletheia.doctor.models.Diagnosis;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DiagnosisWithModelDto {
    private Long id;
    private String name;
    private ModelDto model;

    public DiagnosisWithModelDto(Diagnosis diagnosis) {
        this.id = diagnosis.getId();
        this.name = diagnosis.getName();
        this.model = new ModelDto(diagnosis.getModel());
    }
}
