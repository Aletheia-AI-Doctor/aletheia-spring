package dev.aletheia.doctor.services;
import dev.aletheia.doctor.dtos.votes.VoteDto;
import dev.aletheia.doctor.models.Vote;
import dev.aletheia.doctor.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService extends CRUDService<Vote, VoteDto>  {

    @Autowired
    private VoteRepository VoteRepository;

    protected VoteService() {super(Vote.class, VoteDto.class);}

    public VoteRepository getRepository() {return VoteRepository;}
}
