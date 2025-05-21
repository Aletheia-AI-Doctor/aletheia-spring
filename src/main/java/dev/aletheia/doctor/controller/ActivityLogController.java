package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.services.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class ActivityLogController {
    private final ActivityService activityService;

    public ActivityLogController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<?> getActivityLog() {
        return ResponseEntity.ok(activityService.getAllDTO());
    }
}
