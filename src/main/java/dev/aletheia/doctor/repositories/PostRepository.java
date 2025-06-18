package dev.aletheia.doctor.repositories;

import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.models.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @NotNull
    @Query("SELECT p FROM posts p where p.parent IS NULL ORDER BY p.id DESC")
    List<Post> findAll();

    @NotNull
    @Query("SELECT p FROM posts p where p.parent IS NULL ORDER BY p.id DESC ")
    Page<Post> findAll(@NotNull Pageable pageable);

    @Query("SELECT p FROM posts p WHERE p.doctor.id =?1 and p.parent IS NULL ORDER BY p.id DESC limit 5")
    List<Post> findByDoctorId(Long doctorId);

    @Query("SELECT p FROM posts p WHERE p.parent.id is not NULL and p.parent.doctor.id = ?1 ORDER BY p.id DESC limit 5")
    List<Post> findRepliesByParentId(Long parentId);
}
