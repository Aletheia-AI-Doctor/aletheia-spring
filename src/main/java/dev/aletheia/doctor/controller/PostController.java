package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.PaginationDTO;
import dev.aletheia.doctor.dtos.posts.CreatePostDto;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.services.DoctorService;
import dev.aletheia.doctor.services.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;
	private final DoctorService doctorService;

	public PostController(PostService postService, DoctorService doctorService) {
		this.postService = postService;
		this.doctorService= doctorService;
	}

	@GetMapping
	public ResponseEntity<Object> index(@RequestParam(required = false) Integer page) {
		return ResponseEntity.ok(new PaginationDTO<>(postService.getAllDTO(
				PageRequest.of(page != null ? page : 0, 10)
		)));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Object> show(@PathVariable Long postId) {
		return ResponseEntity.ok(postService.convertToDto(postService.findOrFail(postId)));
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody CreatePostDto createPostDto) {
		Doctor currentDoctor = doctorService.getCurrentDoctor();

		Post post = new Post();
		post.setBody(createPostDto.getBody());
		post.setDoctor(currentDoctor);

		if (createPostDto.getParentId() != null) {
			Post parentPost = postService.findOrFail(createPostDto.getParentId());
			post.setParent(parentPost);
			post.setTitle(null);
		} else {
			if (createPostDto.getTitle() == null || createPostDto.getTitle().isBlank()) {
				return ResponseEntity.badRequest().body(Map.of("message", "Title is required for main posts"));
			}
			post.setTitle(createPostDto.getTitle());
		}
		Post createdPost = postService.save(post);
		return ResponseEntity.status(HttpStatus.CREATED).body(postService.convertToDto(createdPost));

	}
	@PutMapping("/{postId}/edit")
	public ResponseEntity<Object> update(@PathVariable Long postId,
										 @Valid @RequestBody CreatePostDto updatePostDto) {
		Post post = postService.findOrFail(postId);
		Doctor currentDoctor = doctorService.getCurrentDoctor();

		if (!post.getDoctor().getId().equals(currentDoctor.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		if (updatePostDto.getTitle() != null) {
			post.setTitle(updatePostDto.getTitle());
		}

		post.setBody(updatePostDto.getBody());

		Post updatedPost = postService.save(post);
		return ResponseEntity.ok(postService.convertToDto(updatedPost));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> delete(@PathVariable Long postId) {
		Post post = postService.findOrFail(postId);
		Doctor currentDoctor = doctorService.getCurrentDoctor();

		if (!post.getDoctor().getId().equals(currentDoctor.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		postService.delete(postId);
		return ResponseEntity.noContent().build();
	}


}
