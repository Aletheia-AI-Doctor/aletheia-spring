package dev.aletheia.doctor.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "diagnoses")
@Table(name = "diagnoses", indexes = {
        @Index(
                name = "diagnoses_model_id_name_unique",
                columnList = "model_id, name",
                unique = true
        )
})
public class Diagnosis extends BaseModel {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
}
