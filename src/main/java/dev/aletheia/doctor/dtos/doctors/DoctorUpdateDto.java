package dev.aletheia.doctor.dtos.doctors;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class DoctorUpdateDto {
    private String name;
    private String email;
    private String password;
    private String bio;
}
