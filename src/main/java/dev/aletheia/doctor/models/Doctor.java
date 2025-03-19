package dev.aletheia.doctor.models;

import dev.aletheia.doctor.annotations.Unique;
import dev.aletheia.doctor.enums.DoctorSpeciality;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity(name = "doctors")
public class Doctor extends BaseModel {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    @Unique(table = "doctors", value = "username", message = "Username must be unique")
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    @Unique(table = "doctors", value = "email", message = "Email must be unique")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "speciality")
    @Enumerated(EnumType.STRING)
    private DoctorSpeciality speciality;

    @Column(name = "bio", columnDefinition="TEXT")
    private String bio;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patient> patients;

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        this.password = encoder.encode(password);
    }
}
