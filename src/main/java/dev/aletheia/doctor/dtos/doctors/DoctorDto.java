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
    private String email;
    private DoctorSpeciality speciality;
    private String bio;

  /*  public String getSpeciality() {
        return speciality.toString();
    }

    public void setSpeciality(DoctorSpeciality speciality) {
        this.speciality = speciality;
    }*/
}
