package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.models.Vote;
import dev.aletheia.doctor.repositories.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService extends CRUDService<Post, PostDto> {
    @Autowired
    private PostRepository postRepository;

    protected PostService() {super(Post.class, PostDto.class);}

    public PostRepository getRepository() {return postRepository;}
//    @Override
//    public PostDto convertToDto(Post entity) {
//        PostDto dto = modelMapper.map(entity, PostDto.class);
//
//        // Set parent ID if exists
//        if (entity.getParent() != null) {
//            dto.setParentId(entity.getParent().getId());
//        }
//
//        // Set vote information
//        dto.setVotes(voteService.getPostVotes(entity.getId()));
//
//        // Set current user's vote if authenticated
//        try {
//            Doctor currentDoctor = doctorService.getCurrentDoctor();
//            dto.setMyVote(voteService.getRepository()
//                    .findByPostAndDoctor(entity, currentDoctor)
//                    .map(Vote::getValue)
//                    .orElse(0));
//        } catch (Exception e) {
//            dto.setMyVote(0); // Not authenticated or other error
//        }
//
//        return dto;
//    }
}
