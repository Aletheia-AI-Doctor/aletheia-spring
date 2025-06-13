package dev.aletheia.doctor.services;
import dev.aletheia.doctor.dtos.votes.VoteDto;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.models.Vote;
import dev.aletheia.doctor.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService extends CRUDService<Vote, VoteDto>  {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private PostService postService;

    @Autowired
    private DoctorService doctorService;

    protected VoteService() {super(Vote.class, VoteDto.class);}

    public VoteRepository getRepository() {return voteRepository;}

    public Vote vote(Long postId, Integer voteValue) {
        Post post = postService.findOrFail(postId);
        Doctor doctor = doctorService.getCurrentDoctor();

        Vote existingVote = voteRepository.findByPostAndDoctor(post, doctor)
                .orElse(null);

        if (existingVote != null) {
            // If same value, remove the vote (toggle)
            if (existingVote.getValue().equals(voteValue)) {
                voteRepository.delete(existingVote);
                return null;
            }
            // Otherwise update the vote
            existingVote.setValue(voteValue);
            return voteRepository.save(existingVote);
        }

        Vote newVote = new Vote();
        newVote.setPost(post);
        newVote.setDoctor(doctor);
        newVote.setValue(voteValue);

        return voteRepository.save(newVote);
    }

    public Integer getPostVotes(Long postId) {
        return voteRepository.sumVotesByPostId(postId).orElse(0);
    }

    public Integer getMyVote(Long postId) {
        Post post = postService.findOrFail(postId);
        Doctor doctor = doctorService.getCurrentDoctor();
        return voteRepository.findByPostAndDoctor(post, doctor)
                .map(Vote::getValue)
                .orElse(0);
    }

}

