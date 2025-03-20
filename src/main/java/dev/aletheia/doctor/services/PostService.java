package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService extends CRUDService<Post, PostDto> {
    @Autowired
    private PostRepository postRepository;

    protected PostService() {super(Post.class, PostDto.class);}

    public PostRepository getRepository() {return postRepository;}
}
