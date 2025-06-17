package dev.aletheia.doctor.emailservice;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailQueueScheduler {

    private final EmailQueueService queueService;
    private final EmailProcessor emailProcessor;

    public EmailQueueScheduler(EmailQueueService queueService,
                               EmailProcessor emailProcessor) {
        this.queueService = queueService;
        this.emailProcessor = emailProcessor;
    }

    @Scheduled(fixedRate = 60_000) // Run every minute
    public void processEmailQueue() {
        queueService.getPendingEmails(5).forEach(emailProcessor::processEmail);
    }
}