package dev.aletheia.doctor.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
