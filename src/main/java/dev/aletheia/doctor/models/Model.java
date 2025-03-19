package dev.aletheia.doctor.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity(name = "models")
public class Model extends BaseModel {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "path", nullable = false)
    @NotBlank(message = "Path is required")
    private String path;

    @OneToMany(mappedBy = "model")
    private List<Diagnosis> diagnoses;
}
