package dev.aletheia.doctor.dtos.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatePostDto {
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Body must not be empty")
    private String body;

    private Long parentId; // Null for main posts, set for replies
}
