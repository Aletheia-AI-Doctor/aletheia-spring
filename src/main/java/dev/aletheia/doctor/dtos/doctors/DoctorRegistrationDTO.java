package dev.aletheia.doctor.dtos.doctors;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegistrationDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 64, message = "Name must be between 2 and 64 characters")
    private String name;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;

    @NotBlank(message = "Speciality is required")
    private String speciality;

    @NotBlank(message = "License number is required")
    @Pattern(regexp = "\\d+", message = "License number must be numeric")
    private String licenseNumber;

    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;

   
}
