package dev.aletheia.doctor.dtos.models;

import dev.aletheia.doctor.models.Model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ModelDto {
    private Long id;
    private String name;
    private String path;

    public String getSlug() {
        return path;
    }

    public ModelDto(Model model) {
        this.id = model.getId();
        this.name = model.getName();
        this.path = model.getPath();
    }
}
