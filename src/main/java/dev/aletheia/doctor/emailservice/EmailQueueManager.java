package dev.aletheia.doctor.emailservice;

import org.springframework.stereotype.Service;

@Service
public class EmailQueueManager {
    private final EmailQueueService emailQueueService;

    public EmailQueueManager(EmailQueueService emailQueueService) {
        this.emailQueueService = emailQueueService;
    }

    public void queueConfirmationRequest(Long doctorId) {
        EmailData data = new EmailData(doctorId);

        emailQueueService.enqueueEmail(EmailQueue.Type.CONFIRMATION_REQUEST, data);
    }

    public void queueConfirmationDoctor(Long doctorId) {
        EmailData data = new EmailData(doctorId);

        emailQueueService.enqueueEmail(EmailQueue.Type.CONFIRMATION_DOCTOR, data);
    }

    public void queueRejectionDoctor(Long doctorId) {
        EmailData data = new EmailData(doctorId);

        emailQueueService.enqueueEmail(EmailQueue.Type.REJECTION_DOCTOR, data);
    }

}
