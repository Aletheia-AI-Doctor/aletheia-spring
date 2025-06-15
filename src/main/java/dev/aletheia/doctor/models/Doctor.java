package dev.aletheia.doctor.models;

import dev.aletheia.doctor.enums.DoctorSpeciality;
import dev.aletheia.doctor.enums.DoctorStates;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "speciality")
    @Enumerated(EnumType.STRING)
    private DoctorSpeciality speciality;

    @Column(name = "bio", columnDefinition = "TEXT")
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
