package dev.aletheia.doctor.dtos.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UploadScanDto {
    private MultipartFile scan;
}
