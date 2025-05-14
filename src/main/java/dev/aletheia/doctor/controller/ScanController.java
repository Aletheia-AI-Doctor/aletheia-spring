package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.PaginationDTO;
import dev.aletheia.doctor.dtos.scans.SaveScanDto;
import dev.aletheia.doctor.dtos.scans.ScanDto;
import dev.aletheia.doctor.exceptions.NotFoundException;
import dev.aletheia.doctor.services.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/scans")
@RestController
public class ScanController {

    private final ScanService scanService;
    private final DoctorService doctorService;
    private final FileService fileService;

    public ScanController(ScanService scanService, DoctorService doctorService, FileService fileService) {
        this.scanService = scanService;
        this.doctorService = doctorService;
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody SaveScanDto saveScanDto) {
        scanService.create(saveScanDto);

        return ResponseEntity.ok(Map.of("message", "Scan created"));
    }


    @GetMapping
    public ResponseEntity<Object> index(@RequestParam @Nullable Integer page) {
        return ResponseEntity.ok(new PaginationDTO<>(
                scanService.getAllForDoctor(
                        doctorService.getCurrentDoctor(),
                        PageRequest.of(page == null ? 0 : page, 10)
                        )
        ));
    }

    @GetMapping("/{path}/image")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String path) {
        ByteArrayResource image = fileService.getImage(path);

        if(image == null) {
            throw new NotFoundException("Image not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

}
