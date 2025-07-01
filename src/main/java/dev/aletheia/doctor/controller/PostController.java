package dev.aletheia.doctor.controller;

import dev.aletheia.doctor.dtos.PaginationDTO;
import dev.aletheia.doctor.dtos.models.UploadScanDto;
import dev.aletheia.doctor.dtos.notifications.NotificationDto;
import dev.aletheia.doctor.dtos.posts.CreatePostDto;
import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.dtos.posts.UploadImageDto;
import dev.aletheia.doctor.dtos.votes.SetVoteDto;
import dev.aletheia.doctor.exceptions.NotFoundException;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.models.Vote;
import dev.aletheia.doctor.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;
	private final DoctorService doctorService;
	private final VoteService voteService;
	private final DigitalSignService digitalSignService;
	private final FileService fileService;

	@Value("${spring.application.url}")
	private String appUrl;

	public PostController(PostService postService, DoctorService doctorService, VoteService voteService, DigitalSignService digitalSignService, FileService fileService) {
		this.postService = postService;
		this.doctorService= doctorService;
		this.voteService = voteService;
		this.digitalSignService = digitalSignService;
		this.fileService = fileService;
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

	@GetMapping("/{path}/image")
	public ResponseEntity<ByteArrayResource> getImage(@PathVariable String path,
													  @RequestParam @Nullable String token) {
		boolean verify = digitalSignService.verifySignature("/api/posts/" + path + "/image", token);
		if (!verify) {
			throw new NotFoundException("Image not found");
		}

		path = path.replaceAll(",", "/");

		ByteArrayResource image = fileService.getImage(path);

		if (image == null) {
			throw new NotFoundException("Image not found");
		}

		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(image);
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

	@PutMapping("/upload")
	public ResponseEntity<Object> upload(@ModelAttribute UploadImageDto uploadImageDto) throws GeneralSecurityException {
		String imagePath = postService.uploadImage(uploadImageDto.getImage());
		return ResponseEntity.ok(
				Map.of(
						"message", "Image uploaded successfully",
						"imageUrl", appUrl + digitalSignService.getSignedUrl("/api/posts/" + imagePath + "/image")
				)
		);
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
