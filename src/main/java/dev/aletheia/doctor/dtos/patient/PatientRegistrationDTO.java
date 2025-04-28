package dev.aletheia.doctor.dtos.patient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRegistrationDTO {
    private String name;
    private String email;
    private String bio;

}
