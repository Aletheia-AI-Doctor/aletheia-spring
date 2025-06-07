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

    @PostMapping("/post/{postId}")
    public ResponseEntity<Object> votePost(
            @PathVariable Long postId,
            @RequestParam Integer value) {

        // Validate vote value
        if (value == null || (value != 1 && value != -1)) {
            return ResponseEntity.badRequest().body("Vote value must be 1 (upvote) or -1 (downvote)");
        }

        VoteDto result = voteService.votePost(postId, value);

        if (result == null) {
            // Vote was removed
            return ResponseEntity.ok().body(Map.of(
                    "message", "Vote removed",
                    "totalVotes", voteService.getPostVotes(postId)
                    )
            );
        }

        return ResponseEntity.ok(Map.of(
                "vote", result,
                "totalVotes", voteService.getPostVotes(postId)
        ));
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
