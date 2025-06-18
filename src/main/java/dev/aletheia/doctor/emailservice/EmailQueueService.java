package dev.aletheia.doctor.emailservice;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailQueueService {
    private final EmailQueueRepository repository;

    public EmailQueueService(EmailQueueRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void enqueueEmail(EmailQueue.Type type, EmailData data) {
        EmailQueue item = new EmailQueue();
        item.setType(type);
        item.setStatus(EmailQueue.Status.PENDING);
        item.setData(data.toString());

        repository.save(item);
    }

    @Transactional
    public List<EmailQueue> getPendingEmails(int maxRetries) {
        return repository.findByStatusAndLastAttemptBeforeOrLastAttemptIsNull(
                EmailQueue.Status.PENDING,
                LocalDateTime.now().minusMinutes(calculateBackoff(maxRetries))
        );
    }

    @Transactional
    public void markAsSent(Long id) {
        repository.findById(id).ifPresent(item -> {
            item.setStatus(EmailQueue.Status.SENT);
            repository.save(item);
        });
    }

    @Transactional
    public void markForRetry(Long id) {
        repository.incrementRetryCount(id, LocalDateTime.now());
    }

    @Transactional
    public void markAsFailed(Long id) {
        repository.findById(id).ifPresent(item -> {
            item.setStatus(EmailQueue.Status.FAILED);
            repository.save(item);
        });
    }

    private long calculateBackoff(int retryCount) {
        if (retryCount <= 0) {
            return 1; // 1 minute for the first retry
        }
        return Math.min(20, (long) 2 * retryCount);
    }

}
