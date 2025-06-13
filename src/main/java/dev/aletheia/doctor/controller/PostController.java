package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.PaginationDTO;
import dev.aletheia.doctor.dtos.posts.CreatePostDto;
import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.dtos.votes.SetVoteDto;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.models.Vote;
import dev.aletheia.doctor.services.DoctorService;
import dev.aletheia.doctor.services.PostService;
import dev.aletheia.doctor.services.VoteService;
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
	private final VoteService voteService;

	public PostController(PostService postService, DoctorService doctorService, VoteService voteService) {
		this.postService = postService;
		this.doctorService= doctorService;
		this.voteService = voteService;
	}

	@GetMapping
	public ResponseEntity<Object> index(@RequestParam(required = false) Integer page) {
		return ResponseEntity.ok(new PaginationDTO<>(postService.getAllDTO(
				PageRequest.of(page != null ? page : 0, 10)
		)));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Object> show(@PathVariable Long postId) {
		return ResponseEntity.ok(postService.getPostDto(postId));
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
		PostDto dto = postService.convertToDto(createdPost);
		voteService.vote(createdPost.getId(), 1);

		dto.setMyVote(1);

		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
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
		PostDto dto = postService.convertToDto(updatedPost);
		dto.setMyVote(voteService.getMyVote(postId));

		return ResponseEntity.ok(dto);
	}

	@PostMapping("/{postId}/vote")
	public ResponseEntity<Object> vote(@PathVariable Long postId, @RequestBody SetVoteDto voteDto) {
		Vote myVote = voteService.vote(postId, voteDto.getVote() ? 1 : -1);

		return ResponseEntity.ok(Map.of(
				"message", "Vote recorded",
				"votes", voteService.getPostVotes(postId),
				"myVote", myVote != null ? myVote.getValue() : 0
		));
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
