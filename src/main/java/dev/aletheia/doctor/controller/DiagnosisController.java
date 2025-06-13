package dev.aletheia.doctor.controller;
import dev.aletheia.doctor.dtos.models.DiagnosisWithModelDto;
import dev.aletheia.doctor.services.DiagnosisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getAll().stream().map(DiagnosisWithModelDto::new).toList());
    }
}