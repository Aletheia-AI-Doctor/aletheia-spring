package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.scans.SaveScanDto;
import dev.aletheia.doctor.dtos.scans.ScanDto;
import dev.aletheia.doctor.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/scans")
@RestController
public class ScanController {

    private final ScanService scanService;
    private final DoctorService doctorService;

    public ScanController(ScanService scanService, DoctorService doctorService) {
        this.scanService = scanService;
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody SaveScanDto saveScanDto) {
        scanService.create(saveScanDto);

        return ResponseEntity.ok(Map.of("message", "Scan created"));
    }


    @GetMapping
    public ResponseEntity<Object> index() {
        List<ScanDto> scans = scanService.getAllForDoctor(doctorService.getCurrentDoctor());

        return ResponseEntity.ok(Map.of("scans", scans, "message", "Scans fetched"));
    }

}
