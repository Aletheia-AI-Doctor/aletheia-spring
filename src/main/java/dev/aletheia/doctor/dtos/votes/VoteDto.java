package dev.aletheia.doctor.dtos.votes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoteDto {
    private Long id;
    private Long postId;
    private Long doctorId;
    private Integer voteValue;
}
