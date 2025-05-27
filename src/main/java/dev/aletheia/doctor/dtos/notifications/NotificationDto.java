package dev.aletheia.doctor.dtos.notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.aletheia.doctor.dtos.posts.PostDto;
import dev.aletheia.doctor.dtos.votes.VoteDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationDto {
    private long id;
    private long userId;
    private PostDto post;
    private VoteDto vote;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String createdAt;
}
