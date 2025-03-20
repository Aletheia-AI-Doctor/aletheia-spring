package dev.aletheia.doctor.dtos.doctors;

import dev.aletheia.doctor.enums.DoctorSpeciality;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DoctorDto {
    private Long id;
    private String name;
    private String username;
    private DoctorSpeciality speciality;
    private String bio;

    private String getSpeciality() {
        return speciality.toString();
    }

    private void setSpeciality(String speciality) {
        this.speciality = DoctorSpeciality.valueOf(speciality);
    }
}
