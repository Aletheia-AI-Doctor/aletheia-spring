package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.notifications.NotificationDto;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.services.PostService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final PostService postService;

    public NotificationController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/getLastNotification")
    public ResponseEntity<Object> getLastNotification(@RequestParam NotificationDto notificationDto) {
        List<Post> replies = postService.getlastReplies();
        Integer votes = postService.getdoctorsVotes();

        notificationDto.setReplies(replies.stream().map(postService::convertToDto).toList());
        notificationDto.setVote(votes);
        return ResponseEntity.ok(notificationDto);
    }
}
