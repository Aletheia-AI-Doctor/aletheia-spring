package dev.aletheia.doctor.services;

import dev.aletheia.doctor.exceptions.DoctorNotFoundException;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.repositories.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    private PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }
}
