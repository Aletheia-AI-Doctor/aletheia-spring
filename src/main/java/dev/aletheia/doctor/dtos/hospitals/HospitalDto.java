package dev.aletheia.doctor.dtos.hospitals;
import dev.aletheia.doctor.annotations.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HospitalDto {
    private long id;

    @Unique(message = "Hospital already exists with this name", table = "hospitals", value = "name")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters.")
    private String name;

    @Unique(message = "HR email already exists", table = "hospitals", value = "hr_email")
    @Email
    private String hrEmail;
}
