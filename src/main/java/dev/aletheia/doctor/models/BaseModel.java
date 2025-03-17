package dev.aletheia.doctor.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
abstract public class BaseModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
}
