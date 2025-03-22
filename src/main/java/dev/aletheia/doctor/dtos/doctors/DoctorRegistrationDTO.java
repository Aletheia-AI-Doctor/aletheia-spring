package dev.aletheia.doctor.dtos.doctors;

import dev.aletheia.doctor.enums.DoctorSpeciality;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegistrationDTO {
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBio() {
        return bio;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
    private String name;
    private String username;
    private String email;
    private String password;
    private String speciality;
    private String bio;

    public DoctorSpeciality getSpeciality() {
        return DoctorSpeciality.valueOf(speciality);
    }
}
