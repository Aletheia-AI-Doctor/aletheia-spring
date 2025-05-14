package dev.aletheia.doctor.dtos.doctors;

import dev.aletheia.doctor.enums.DoctorSpeciality;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegistrationDTO {
    private String name;
    private String username;
    private String email;
    private String password;
    private String speciality;
    private String license_number;
    private Long hospital_id;

   /* public DoctorSpeciality getSpeciality() {
        return DoctorSpeciality.valueOf(speciality);
    }*/
}
