package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.models.UploadScanDto;
import dev.aletheia.doctor.services.ModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/models")
public class ModelController {

	private final ModelService modelService;

	public ModelController(ModelService modelService) {
		this.modelService = modelService;
	}

	@GetMapping
	public ResponseEntity<Object> index() {
		return ResponseEntity.ok(modelService.getAllDTO());
	}

	@GetMapping("/{modelSlug}")
	public ResponseEntity<Object> show(@PathVariable String modelSlug) {
		return ResponseEntity.ok(modelService.convertToDto(
				modelService.getBySlug(modelSlug))
		);
	}

	@PutMapping("/{modelSlug}/predict")
	public ResponseEntity<Object> predict(@ModelAttribute UploadScanDto uploadScanDto,
										 @PathVariable String modelSlug) {
		return ResponseEntity.ok(
				modelService.predict(
						modelService.getBySlug(modelSlug),
						uploadScanDto.getScan()
				)
		);
	}

}
