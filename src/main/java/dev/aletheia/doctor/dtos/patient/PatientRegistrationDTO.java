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
    private Gender sex;
    private PatientStatus status;
    private void setStatus(){
        this.status=PatientStatus.DISCHARGED;
    }
    public PatientStatus getStatus(){
        return status;
    }
    
}
