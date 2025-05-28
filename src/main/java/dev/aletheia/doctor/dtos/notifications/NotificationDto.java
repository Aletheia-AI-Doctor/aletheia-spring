package dev.aletheia.doctor.dtos.notifications;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.dtos.votes.VoteDto;
import dev.aletheia.doctor.models.Post;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationDto {
    private long id;
    private long userId;
    private List<Post> replies;
    private PostDto poste;
    private Integer vote;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String createdAt;
}
