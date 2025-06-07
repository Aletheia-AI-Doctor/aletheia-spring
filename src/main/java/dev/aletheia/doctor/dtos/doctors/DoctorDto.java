package dev.aletheia.doctor.dtos.doctors;

import dev.aletheia.doctor.enums.DoctorSpeciality;
import dev.aletheia.doctor.helpers.HashUtil;
import dev.aletheia.doctor.models.Hospital;
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
    private String licenseNumber;
    private Long hospitalId;
    private String image;

    public String getImage() {
        return "https://gravatar.com/avatar/" + HashUtil.sha256Hex(this.email) + "?d=identicon&s=200";
    }

   /*public String getSpeciality() {
        return speciality.toString();
    }

    public void setSpeciality(DoctorSpeciality speciality) {
        this.speciality = speciality;
    }*/

}

