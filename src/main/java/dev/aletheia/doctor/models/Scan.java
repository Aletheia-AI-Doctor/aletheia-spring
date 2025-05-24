package dev.aletheia.doctor.models;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "scans")
public class Scan extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "model_diagnosis_id")
    private Diagnosis modelDiagnosis;

    @ManyToOne
    @JoinColumn(name = "doctor_diagnosis_id")
    private Diagnosis doctorDiagnosis;

    @Column(name = "image")
    private String image;

    @Column(name = "image_response")
    private String imageResponse;
}
