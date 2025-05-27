package dev.aletheia.doctor.controller;


import dev.aletheia.doctor.services.PostService;
import dev.aletheia.doctor.services.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final PostService postService;
    private final VoteService voteService;

    public NotificationController(PostService postService, VoteService voteService) {
        this.postService = postService;
        this.voteService = voteService;
    }
    @GetMapping("/notifications")
    public ResponseEntity<Object> index() {
        postService.getAllDTO();
        voteService.getAllDTO();

        return ResponseEntity.ok("Notifications endpoint is under construction.");
    }


}
