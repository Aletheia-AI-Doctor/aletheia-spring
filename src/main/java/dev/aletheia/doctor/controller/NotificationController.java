package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.notifications.NotificationDto;
import dev.aletheia.doctor.dtos.posts.PostDto;
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
public ResponseEntity<NotificationDto> getLastNotification(NotificationDto notificationDto) {
    List<PostDto> replies = postService.getLastReplies();

    List<Post> recentPosts = postService.getRecentPostsByDoctor();
    List<Integer> votes = postService.getVotesForPosts(recentPosts);
    List<PostDto> posts = postService.convertToDto(recentPosts);

    notificationDto.setReplies(replies);
    notificationDto.setVotes(votes);
    notificationDto.setPosts(posts);

    return ResponseEntity.ok(notificationDto);
}

}
