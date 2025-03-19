package dev.aletheia.doctor.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "votes", indexes = {
        @Index(
                name = "votes_post_doctor_unique",
                columnList = "post_id, doctor_id",
                unique = true
        )
})
@Entity
public class Vote extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "value", nullable = false, columnDefinition = "TINYINT")
    private Integer value;
}
