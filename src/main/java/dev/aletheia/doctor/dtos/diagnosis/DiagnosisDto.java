package dev.aletheia.doctor.dtos.diagnosis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiagnosisDto {

    private Long id;
    private String name;
    private Long modelId;

    public DiagnosisDto(Long id, String name, Long modelId) {
        this.id = id;
        this.name = name;
        this.modelId = modelId;
    }
}
