package dev.aletheia.doctor.dtos.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.aletheia.doctor.dtos.doctors.DoctorDto;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Setter
@Getter
public class PostDto {
    private Long id;
    private String title;
    private String body;
    private DoctorDto doctor;
    private List<PostDto> replies;
    private Integer votes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String createdAt;
}
