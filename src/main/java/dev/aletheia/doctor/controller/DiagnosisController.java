package dev.aletheia.doctor.controller;


import dev.aletheia.doctor.models.Model;
import dev.aletheia.doctor.services.DiagnosisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping("/getByModel")
    public ResponseEntity<Object> getByModel(@RequestParam Model modelName) {
        return ResponseEntity.ok(diagnosisService.getByModel(modelName));
    }
}
