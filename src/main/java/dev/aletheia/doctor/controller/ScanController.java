package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.scans.SaveScanDto;
import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.models.Model;
import dev.aletheia.doctor.models.Patient;
import dev.aletheia.doctor.models.Scan;
import dev.aletheia.doctor.services.DiagnosisService;
import dev.aletheia.doctor.services.ModelService;
import dev.aletheia.doctor.services.PatientService;
import dev.aletheia.doctor.services.ScanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/scans")
@RestController
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody SaveScanDto saveScanDto) {
        Scan scan = scanService.create(saveScanDto);


        return ResponseEntity.ok("Scan saved");
    }

}
