package dev.aletheia.doctor.models;

import dev.aletheia.doctor.enums.Gender;
import dev.aletheia.doctor.enums.PatientStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Setter
@Getter
@Entity(name = "patients")
public class Patient extends BaseModel {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Scan> scans;

    @Column(name = "birthdate")
    @NotNull(message = "Birthdate is required")
    @PastOrPresent(message = "Birthdate cannot be in the future")
    private LocalDate birthdate;

    @Column(name = "sex")
    @NotNull(message = "Sex is required")
    @Enumerated(EnumType.STRING)
    private Gender sex;

    

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PatientStatus status;

    @Column(name = "admission_date")
    private LocalDate admissionDate;
}
