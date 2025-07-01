package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.notifications.NotificationDto;
import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.dtos.posts.UploadImageDto;
import dev.aletheia.doctor.models.BaseModel;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.models.Vote;
import dev.aletheia.doctor.repositories.PostRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.aletheia.doctor.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostService extends CRUDService<Post, PostDto> {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private FileService fileService;

    protected PostService() {
        super(Post.class, PostDto.class);
    }

    public PostRepository getRepository() {
        return postRepository;
    }

    private List<Long> getAllPostIds(Post post) {
        List<Long> postIds = new ArrayList<>();
        postIds.add(post.getId());

        if (post.getReplies() != null) {
            for (Post reply : post.getReplies()) {
                postIds.addAll(getAllPostIds(reply));
            }
        }

        return postIds;
    }

    private Map<Long, Integer> getVotesForPosts(List<Long> postIds, Doctor doctor) {
        return voteRepository
                .findMyVotes(postIds, doctor.getId())
                .stream()
                .collect(Collectors.toMap(vote -> vote.getPost().getId(), Vote::getValue));
    }

    private PostDto addMyVotes(PostDto postDto, Map<Long, Integer> myVotes) {
        postDto.setMyVote(myVotes.getOrDefault(postDto.getId(), 0));

        if (postDto.getReplies() != null) {
            List<PostDto> repliesDto = postDto.getReplies().stream()
                    .map(reply -> addMyVotes(reply, myVotes))
                    .collect(Collectors.toList());

            postDto.setReplies(repliesDto);
        }

        return postDto;
    }

    public PostDto getPostDto(Long postId) {
        Post post = findOrFail(postId);
        PostDto postDto = convertToDto(post);
        Doctor currentDoctor = doctorService.getCurrentDoctor();

        if (currentDoctor != null) {
            Map<Long, Integer> myVotes = getVotesForPosts(getAllPostIds(post), currentDoctor);
            return addMyVotes(postDto, myVotes);
        }

        return postDto;
    }

    public Page<PostDto> getAllDTO(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Doctor currentDoctor = doctorService.getCurrentDoctor();
        if (currentDoctor == null) {
            return posts.map(this::convertToDto);
        }

        Map<Long, Integer> myVotes = getVotesForPosts(posts.stream().map(Post::getId).toList(), currentDoctor);

        return posts.map(post -> {
            PostDto dto = convertToDto(post);
            dto.setMyVote(myVotes.getOrDefault(post.getId(), 0));
            return dto;
        });
    }

    public List<Post> getRecentPostsByDoctor() {
        Doctor doctor = doctorService.getCurrentDoctor();
        return postRepository.findByDoctorId(doctor.getId()); // already limited to 5
    }

    public List<Integer> getVotesForPosts(List<Post> posts) {
        return posts.stream()
                .map(post -> voteRepository.sumVotesByPostId(post.getId()).orElse(0))
                .toList();
    }

    public List<PostDto> convertToDto(List<Post> posts) {
        return posts.stream()
                .map(this::convertToDto)
                .toList();
    }


    public List<PostDto> getLastReplies() {
        Doctor doctor = doctorService.getCurrentDoctor();

        return postRepository.findRepliesByParentId(doctor.getId()).stream()
                .map(this::convertToDto)
                .toList();
    }

    public String uploadImage(MultipartFile image) {
        return fileService.saveFile(image);
    }
}
