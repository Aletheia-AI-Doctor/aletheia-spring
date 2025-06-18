package dev.aletheia.doctor.emailservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DigitalSignService;
import dev.aletheia.doctor.services.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailProcessor {

    private static final Logger log = LoggerFactory.getLogger(EmailProcessor.class);
    private final EmailSender emailSender;
    private final EmailQueueService queueService;
    private final ObjectMapper objectMapper;
    private final int MAX_RETRIES = 5;
    private final DoctorService doctorService;
    private final DigitalSignService digitalSignService;

    @Value("${spring.application.url}")
    private String appUrl;

    @Value("${spring.application.frontend_url}")
    private String frontendUrl;

    public EmailProcessor(EmailSender emailSender,
                          EmailQueueService queueService,
                          ObjectMapper objectMapper, DoctorService doctorService, DigitalSignService digitalSignService) {
        this.emailSender = emailSender;
        this.queueService = queueService;
        this.objectMapper = objectMapper;
        this.doctorService = doctorService;
        this.digitalSignService = digitalSignService;
    }

    @Transactional
    public void processEmail(EmailQueue item) {
        EmailData reqData;
        try {
            reqData = objectMapper.readValue(item.getData(), EmailData.class);
        } catch (JsonProcessingException e) {
            handleFailure(item, e);
            return;
        }

        Doctor doctor = doctorService.findOrFail(reqData.getDoctorId());

        try {
            switch (item.getType()) {
                case CONFIRMATION_REQUEST:
                    emailSender.sendConfirmationRequest(
                            doctor.getHospital().getHrEmail(),
                            doctor.getName(),
                            doctor.getSpeciality().name(),
                            doctor.getLicenseNumber(),
                            getConfirmationLink(doctor),
                            getRejectionLink(doctor)
                    );
                    break;
                case CONFIRMATION_DOCTOR:
                    emailSender.sendConfirmationDoctor(
                            doctor.getEmail(),
                            doctor.getName(),
                            doctor.getHospital().getName(),
                            getLoginLink()
                    );
                    break;

                case REJECTION_DOCTOR:
                    emailSender.sendRejectionDoctor(
                            doctor.getEmail(),
                            doctor.getName(),
                            doctor.getHospital().getName(),
                            getAppealLink(doctor)
                    );
                    break;
            }

            queueService.markAsSent(item.getId());
        } catch (Exception e) {
            handleFailure(item, e);
        }
    }

    private String getLoginLink() {
        return frontendUrl + "/login";
    }

    private String getConfirmationLink(Doctor doctor) {
        return getSignedUrl(appUrl + "/api/confirm-email/" + doctor.getId());
    }

    private String getRejectionLink(Doctor doctor) {
        return getSignedUrl(appUrl + "/api/reject-email/" + doctor.getId());
    }

    private String getAppealLink(Doctor doctor) {
        return getSignedUrl(appUrl + "/api/doctors/appeal/" + doctor.getId());
    }

    private String getSignedUrl(String url) {
        try {
            return digitalSignService.getSignedUrl(url);
        } catch (Exception e) {
            return "";
        }
    }

    private void handleFailure(EmailQueue item, Exception e) {
        log.error("Failed to process email item {}: {}", item.getId(), e.getMessage());

        if (item.getRetryCount() >= MAX_RETRIES) {
            item.setStatus(EmailQueue.Status.FAILED);

        } else {
            queueService.markForRetry(item.getId());
        }
    }
}