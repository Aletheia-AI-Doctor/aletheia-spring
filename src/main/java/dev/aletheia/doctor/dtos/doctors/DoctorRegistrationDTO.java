package dev.aletheia.doctor.dtos.doctors;

import dev.aletheia.doctor.annotations.Unique;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegistrationDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 128, message = "Name must be between 2 and 128 characters.")
    private String name;

    @NotBlank(message = "Username is required")
    @Unique(table = "doctors", value = "username", message = "Username must be unique.")
    @Size(min = 1, max = 128, message = "Username maximum 128 characters.")
    private String username;

    @Size(min = 3, max = 128, message = "Email must be between 3 and 255 characters.")
    @Unique(table = "doctors", value = "email", message = "Email must be unique.")
    @Email(message = "Email must be a valid email address.")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$",
            message = "Password must be at least 6 characters long and contain uppercase, lowercase, number, and special character.")
    private String password;

    @NotBlank(message = "Speciality is required")
    private String speciality;

    @Size(min = 1, max = 64, message = "License number.")
    @Pattern(regexp = "\\d+", message = "License number must be a number")
    @Unique(table = "doctors", value = "license_number", message = "This license number is already registered.")
    private String licenseNumber;

    @NotNull(message = "Hospital is required")
    private Long hospitalId;
}
