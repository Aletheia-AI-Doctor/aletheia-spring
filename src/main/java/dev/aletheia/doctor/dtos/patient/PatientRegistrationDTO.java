package dev.aletheia.doctor.dtos.patient;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRegistrationDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 100 characters")
    private String name;

    @PastOrPresent(message = "Birthdate cannot be in the future")
    private LocalDate birthdate;

    @Pattern(regexp = "^(male|female)$", message = "Sex must be either male or female")
    @NotBlank(message = "Sex is required")
    private String sex;
}
