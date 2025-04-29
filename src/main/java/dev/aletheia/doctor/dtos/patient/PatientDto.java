package dev.aletheia.doctor.dtos.patient;


import java.time.LocalDate;

import dev.aletheia.doctor.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientDto {
    private Long doctor_id;
    private String name;
    private  Gender sex;
    private LocalDate addmissionDate ;
    private String status;


}
