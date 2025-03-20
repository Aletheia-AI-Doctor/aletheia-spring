package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping
	public ResponseEntity<Object> index() {
		return ResponseEntity.ok(postService.getAllDTO());
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Object> show(@PathVariable Long postId) {
		return ResponseEntity.ok(postService.convertToDto(postService.findOrFail(postId)));
	}

}
