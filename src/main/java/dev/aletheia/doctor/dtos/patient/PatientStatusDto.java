package dev.aletheia.doctor.dtos.patient;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientStatusDto {
    @Pattern(regexp = "^(PENDING|DIAGNOSED)$", message = "Status must either pending or diagnosed")
    private String status;
}
