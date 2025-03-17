package dev.aletheia.doctor.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "doctors")
public class Doctor extends BaseModel {

    @Column(name = "name")
    private String name;
}
