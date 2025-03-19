package dev.aletheia.doctor.models;

import dev.aletheia.doctor.enums.Gender;
import dev.aletheia.doctor.enums.PatientStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity(name = "scans")
public class Scan extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "model_diagnosis_id")
    private Diagnosis modelDiagnosis;

    @ManyToOne
    @JoinColumn(name = "doctor_diagnosis_id")
    private Diagnosis doctorDiagnosis;

    @Column(name = "image")
    private String image;
}
