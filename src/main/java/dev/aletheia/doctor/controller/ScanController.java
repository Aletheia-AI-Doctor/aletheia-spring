package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.PaginationDTO;
import dev.aletheia.doctor.dtos.scans.SaveScanDto;
import dev.aletheia.doctor.exceptions.NotFoundException;
import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.models.Patient;
import dev.aletheia.doctor.models.Scan;
import dev.aletheia.doctor.services.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/scans")
@RestController
public class ScanController {

    private final ScanService scanService;
    private final DoctorService doctorService;
    private final FileService fileService;
    private final PatientService patientService;

    private final DiagnosisService diagnosisService;

    public ScanController(ScanService scanService, DoctorService doctorService, FileService fileService, PatientService patientService, DiagnosisService diagnosisService) {
        this.scanService = scanService;
        this.doctorService = doctorService;
        this.fileService = fileService;
        this.patientService = patientService;
        this.diagnosisService = diagnosisService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody SaveScanDto saveScanDto) {
        scanService.create(saveScanDto);

        return ResponseEntity.ok(Map.of("message", "Scan created"));
    }


    @GetMapping
    public ResponseEntity<Object> index(@RequestParam @Nullable Integer page, @RequestParam @Nullable Long patientId) {
        Patient patient = null;
        if (patientId != null) {
            patient = patientService.findOrFail(patientId);
        }

        return ResponseEntity.ok(new PaginationDTO<>(
                scanService.getAllForDoctor(
                        doctorService.getCurrentDoctor(),
                        patient,
                        PageRequest.of(page == null ? 0 : page, 10)
                )
        ));
    }

    @GetMapping("/{path}/image")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String path) {
        path = path.replaceAll(",", "/");

        ByteArrayResource image = fileService.getImage(path);

        if (image == null) {
            throw new NotFoundException("Image not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }


    @PatchMapping("/{id}/doctor-diagnosis")
    public ResponseEntity<Object> setDoctorDiagnosis(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String diagnosisId = request.get("diagnosis");
        if (diagnosisId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Diagnosis is required"));
        }

        Scan scan = scanService.findOrFail(id);
        Diagnosis diagnosis = diagnosisService.findOrFail(Long.parseLong(diagnosisId));

        // Validate that the diagnosis belongs to the scan's model
        if (!diagnosis.getModel().equals(scan.getModelDiagnosis().getModel())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Diagnosis does not belong to the scan's model"));
        }

        scanService.setDoctorDiagnosis(scan, diagnosis);
        return ResponseEntity.ok(Map.of("message", "Scan updated"));
    }

}
