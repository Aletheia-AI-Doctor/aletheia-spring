package dev.aletheia.doctor.dtos.votes;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoteDto {
    private Long id;
    private Long postId;
    private Long doctorId;
    private Integer voteValue; // 1 for upvote, -1 for downvote

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String createdAt;


}
