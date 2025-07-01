package dev.aletheia.doctor.dtos.posts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UploadImageDto {
    private MultipartFile image;
}
