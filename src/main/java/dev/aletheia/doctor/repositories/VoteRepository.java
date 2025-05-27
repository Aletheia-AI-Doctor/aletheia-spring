package dev.aletheia.doctor.repositories;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.models.Post;
import dev.aletheia.doctor.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPostAndDoctor(Post post, Doctor doctor);

    @Query("SELECT COALESCE(SUM(v.value), 0) FROM Vote v WHERE v.post.id = :postId")
    Optional<Integer> sumVotesByPostId(@Param("postId") Long postId);
}
