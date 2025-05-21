package dev.aletheia.doctor.models;

import dev.aletheia.doctor.annotations.Unique;
import dev.aletheia.doctor.enums.DoctorSpeciality;
import dev.aletheia.doctor.enums.DoctorStates;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity(name = "doctors")
@DynamicUpdate
public class Doctor extends BaseModel {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    // @Unique(table = "doctors", value = "username", message = "Username must be unique")
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column(name = "license_number", nullable = false, unique = true)
    // @Unique(table = "doctors", value = "license_number", message = "license_number must be unique")
    @NotBlank(message = "license_number is mandatory")
    private String licenseNumber;

    @Column(name = "email", nullable = false, unique = true)
    // @Unique(table = "doctors", value = "email", message = "Email must be unique")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 6, max = 255, message = "Password should be at least 6 characters")
    private String password;

    @Column(name = "speciality")
    @Enumerated(EnumType.STRING)
    private DoctorSpeciality speciality;

    @Column(name = "bio", columnDefinition="TEXT")
    private String bio;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patient> patients;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DoctorStates status;

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        this.password = encoder.encode(password);
    }

    public boolean checkPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        return encoder.matches(password, this.password);
    }

    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_DOCTOR"));
    }



    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + getId() + '\'' +
                " name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", license_number='" + licenseNumber + '\'' +
                ", speciality=" + speciality +
                ", bio='" + bio + '\'' +
                ", confirmedAt=" + confirmedAt +
                ", hospital=" + hospital +
                '}';
    }
}
