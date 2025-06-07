package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.models.Vote;
import dev.aletheia.doctor.repositories.PostRepository;
import jakarta.validation.constraints.Null;

import java.util.List;

import javax.print.Doc;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService extends CRUDService<Post, PostDto> {
    @Autowired
    private PostRepository postRepository;
    private DoctorService doctorService;
    private PostRepository post;

    protected PostService() {super(Post.class, PostDto.class);}

    public PostRepository getRepository() {return postRepository;}
    
    
    public Integer getdoctorsVotes(){
        Doctor doctor = doctorService.getCurrentDoctor();
        if (doctor == null) {
            return 0; 
        }
        Post posts = post.findByDoctorId(doctor.getId());
        if (posts == null) {
            return 0; 
        }
        return posts.getVotes() != null ? posts.getVotes() : 0;
    }

    public List<Post> getlastReplies(){
        Doctor doctor = doctorService.getCurrentDoctor();
        if (doctor == null) {
            return null; 
        }
        List<Post> replies = post.findRepliesByParentId(doctor.getId());
       
        if (replies==null){
            return null;
        }
        
        return replies;
        
    }


}
