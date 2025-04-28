package dev.aletheia.doctor.dtos.patient;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientDto {
    private Long id;
    private String name;
    private String username;
    private String bio;
    
}
