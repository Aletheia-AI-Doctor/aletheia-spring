package dev.aletheia.doctor.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.List;

@Setter
@Getter
@Entity(name = "posts")
@Table
public class Post extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "parent_id", updatable = false)
    private Post parent;

    @Column(name = "title")
    @Size(min = 1, max = 255)
    private String title;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String body;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Post> replies;

    @Formula("(SELECT COALESCE(SUM(v.value), 0) FROM votes v WHERE v.post_id = id)")
    private Integer votes;
}
