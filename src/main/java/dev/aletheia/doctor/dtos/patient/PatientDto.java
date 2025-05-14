package dev.aletheia.doctor.dtos.patient;


import java.time.LocalDate;

import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import dev.aletheia.doctor.enums.Gender;
import dev.aletheia.doctor.models.Doctor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientDto {
    private DoctorDto doctor;
    private Long id;
    private String name;
    private String sex;
    private LocalDate admissionDate;
    private String status;
}
