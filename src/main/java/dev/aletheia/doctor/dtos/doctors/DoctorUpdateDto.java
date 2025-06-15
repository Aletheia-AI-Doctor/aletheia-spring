package dev.aletheia.doctor.dtos.doctors;

import dev.aletheia.doctor.annotations.UniqueIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@UniqueIgnore(
        table = "doctors",
        fields = {"email", "username"},
        columns = {"email", "username"}
)
public class DoctorUpdateDto {
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 128, message = "Username maximum 128 characters.")
    private String username;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 128, message = "Name must be between 2 and 128 characters.")
    private String name;

    @Size(min = 3, max = 128, message = "Email must be between 3 and 255 characters.")
    @Email(message = "Email must be a valid email address.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$",
            message = "Password must be at least 6 characters long and contain uppercase, lowercase, number, and special character.")
    private String password;

    @Size(max = 1024, message = "Bio must be at most 1024 characters long.")
    private String bio;
}