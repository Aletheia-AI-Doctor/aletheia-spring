package dev.aletheia.doctor.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;


@Service
public class FileService {

    @Autowired
    private HttpServletRequest request;

    @Value("${models.upload_path}")
    private String UPLOAD_DIR;

    public String saveFile(MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();


            }

            // Generate a unique file name
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID() + extension;

            // Construct the file path
            String filePath = UPLOAD_DIR + uniqueFileName;
            File dest = new File(filePath);

            file.transferTo(dest);
            return uniqueFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ByteArrayResource getImage(String path) {
        String filePath = UPLOAD_DIR + path;

        File file = new File(filePath);

        if (! file.exists()) {
            return null;
        }

        try {
            return new ByteArrayResource(Files.readAllBytes(file.getAbsoluteFile().toPath()));
        } catch (IOException e) {
            return null;
        }
    }
}
