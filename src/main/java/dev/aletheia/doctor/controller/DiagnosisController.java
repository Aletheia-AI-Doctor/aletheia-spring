package dev.aletheia.doctor.controller;


import dev.aletheia.doctor.models.Diagnosis;
import dev.aletheia.doctor.models.Model;
import dev.aletheia.doctor.repositories.ModelRepository;
import dev.aletheia.doctor.services.DiagnosisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final ModelRepository modelRepository;

    public DiagnosisController(DiagnosisService diagnosisService, ModelRepository modelRepository) {
        this.diagnosisService = diagnosisService;
        this.modelRepository = modelRepository;
    }

    @GetMapping
    public ResponseEntity<List<Diagnosis>> getAllDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getAll());
    }

    @GetMapping("/byModel")
    public ResponseEntity<Object> getDiagnosis(@RequestBody Long modelId){
        Optional<Model> model = modelRepository.findById(modelId);
        Optional<Diagnosis> diagnoses = diagnosisService.getAllDiagnoses(model);
        return ResponseEntity.ok(diagnoses.stream().map(diagnosisService::convertToDto).toList());
    }
}
