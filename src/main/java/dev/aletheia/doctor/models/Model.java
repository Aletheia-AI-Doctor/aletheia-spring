package dev.aletheia.doctor.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "models")
public class Model extends BaseModel {

    public Model(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "path", nullable = false, unique = true)
    @NotBlank(message = "Path is required")
    private String path;

    @OneToMany(mappedBy = "model", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Diagnosis> diagnoses;

    public void addDiagnosis(Diagnosis diagnosis) {
        if (this.diagnoses == null) {
            this.diagnoses = new ArrayList<>();
        }

        diagnosis.setModel(this);
        this.diagnoses.add(diagnosis);
    }
}
