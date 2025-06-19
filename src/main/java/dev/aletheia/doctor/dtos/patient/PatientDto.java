package dev.aletheia.doctor.dtos.patient;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String admissionDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String birthdate;
    private String status;
}
