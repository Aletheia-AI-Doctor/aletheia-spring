package dev.aletheia.doctor.dtos.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModelDto {
    private String id;
    private String name;
    private String path;

    public String getSlug() {
        return path;
    }
}
