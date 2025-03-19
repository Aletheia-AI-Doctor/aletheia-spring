package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
