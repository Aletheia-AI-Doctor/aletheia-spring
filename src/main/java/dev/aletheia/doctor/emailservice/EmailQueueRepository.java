package dev.aletheia.doctor.emailservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailQueueRepository extends JpaRepository<EmailQueue, Long> {

    List<EmailQueue> findByStatusAndLastAttemptBeforeOrLastAttemptIsNull(
            EmailQueue.Status status,
            LocalDateTime threshold
    );

    @Transactional
    @Modifying
    @Query("UPDATE EmailQueue e SET e.retryCount = e.retryCount + 1, e.lastAttempt = :now WHERE e.id = :id")
    void incrementRetryCount(Long id, LocalDateTime now);
}