package dev.aletheia.doctor.models;

import dev.aletheia.doctor.enums.Gender;
import dev.aletheia.doctor.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity(name = "patients")
public class Patient extends BaseModel {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Gender sex;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PatientStatus status;

    @Column(name = "addmission_date")
    private LocalDate addmissionDate;
}
