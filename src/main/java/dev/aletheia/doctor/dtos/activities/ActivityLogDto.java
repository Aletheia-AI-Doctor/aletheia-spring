package dev.aletheia.doctor.dtos.activities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ActivityLogDto {
    private Long id;
    private String action;
    private String description;
    private LocalDateTime createdAt;
}
