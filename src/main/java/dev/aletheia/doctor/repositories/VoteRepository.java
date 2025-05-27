package dev.aletheia.doctor.repositories;
import dev.aletheia.doctor.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
