package dev.aletheia.doctor.emailservice;

import dev.aletheia.doctor.models.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_queue")
@Getter
@Setter
public class EmailQueue extends BaseModel {

    public enum Status { PENDING, SENT, FAILED }
    public enum Type { CONFIRMATION_REQUEST, CONFIRMATION_DOCTOR, REJECTION_DOCTOR }

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name="data", columnDefinition = "JSON")
    private String data;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Column(name = "last_attempt")
    private LocalDateTime lastAttempt;
}