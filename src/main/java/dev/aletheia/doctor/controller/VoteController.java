package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.services.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
