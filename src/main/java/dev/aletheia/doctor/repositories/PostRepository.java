package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM posts p where p.parent IS NULL ORDER BY p.id DESC")
    List<Post> findAll();
}
