package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.votes.VoteDto;
import dev.aletheia.doctor.services.DoctorService;
import dev.aletheia.doctor.services.PostService;
import dev.aletheia.doctor.services.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping
    public ResponseEntity<Object> index() {
        return ResponseEntity.ok(voteService.getAllDTO());
    }

    @GetMapping("/{voteId}")
    public ResponseEntity<Object> show(@PathVariable Long voteId) {
        return ResponseEntity.ok(voteService.convertToDto(voteService.findOrFail(voteId)));
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Integer> getPostVotes(@PathVariable Long postId) {
        return ResponseEntity.ok(voteService.getPostVotes(postId));
    }

    @GetMapping("/post/{postId}/my-vote")
    public ResponseEntity<Integer> getMyVote(@PathVariable Long postId) {
        return ResponseEntity.ok(voteService.getMyVote(postId));
    }
}
