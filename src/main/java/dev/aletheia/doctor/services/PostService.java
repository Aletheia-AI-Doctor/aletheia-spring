package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.repositories.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService extends CRUDService<Post, PostDto> {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;

    protected PostService() {super(Post.class, PostDto.class);}

    public PostRepository getRepository() {return postRepository;}
    @Override
    public PostDto convertToDto(Post entity) {
        PostDto dto = modelMapper.map(entity, PostDto.class);
        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
        }
        return dto;
    }
}
