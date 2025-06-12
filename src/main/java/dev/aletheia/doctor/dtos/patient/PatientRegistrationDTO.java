package dev.aletheia.doctor.dtos.patient;


import java.time.LocalDate;

import dev.aletheia.doctor.enums.Gender;
import dev.aletheia.doctor.enums.PatientStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRegistrationDTO {
    private String name;
    private LocalDate birthdate;
    private String sex;
    private String status;


    public void setBirthdate(LocalDate birthdate) {
        if (birthdate != null && !birthdate.isAfter(LocalDate.now())) {
            this.birthdate = birthdate;
        } else {
            throw new IllegalArgumentException("Birthdate must be a valid date and not in the future");
        }
    }
}
