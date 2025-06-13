package dev.aletheia.doctor.dtos.doctors;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegistrationDTO {

   
    private String name;

   
    private String username;

    
    private String email;

    
    private String password;

   
    private String speciality;

    private String licenseNumber;

    private Long hospitalId;

    public void setLicenseNumber(String licenseNumber) {
        if (licenseNumber == null || !licenseNumber.matches("\\d+")) {
            throw new IllegalArgumentException("License number must be numeric.");
        }
        this.licenseNumber = licenseNumber;
    }
    public void setPassword(String password) {
        if (password != null && !password.isBlank()) {
            if (!isStrongPassword(password)) {
                throw new IllegalArgumentException("Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character.");
            }
        }
        this.password = password;
    }

    public void setEmail(String email) {
        if (email != null && !email.isBlank()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException("Invalid email format.");
            }
        }
        this.email = email;
    }

    private boolean isStrongPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(regex);
    }
    
    public void setName(String name) {
        if (name != null && (name.length() < 2 || name.length() > 64)) {
            throw new IllegalArgumentException("Name must be between 2 and 64 characters.");
        }
        this.name = name;
    }

}
