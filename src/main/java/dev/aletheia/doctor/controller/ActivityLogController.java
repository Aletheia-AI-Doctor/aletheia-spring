package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.PaginationDTO;
import dev.aletheia.doctor.services.ActivityService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class ActivityLogController {
    private final ActivityService activityService;

    public ActivityLogController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentActivityLogs() {
        return ResponseEntity.ok(activityService.getAllDTO());
    }

    @GetMapping
    public ResponseEntity<?> getAllActivityLogs(@RequestParam(required = false) Integer page) {
        return ResponseEntity.ok(
                new PaginationDTO<>(activityService.getAllDTO(PageRequest.of(page == null ? 0 : page, 10)))
        );
    }
}
