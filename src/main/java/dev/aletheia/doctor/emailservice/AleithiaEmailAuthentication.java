package dev.aletheia.doctor.emailservice;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class AleithiaEmailAuthentication {

    private final JavaMailSender mailSender;

    @Autowired
    public AleithiaEmailAuthentication(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationRequest(String hrEmail, String doctorName, String doctorSpeciality,
                                        String doctorLicenceNumber, String confirmationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(hrEmail);
            helper.setSubject("Doctor Registration Confirmation: " + doctorName);

            String htmlContent = "<html><body>" +
                    "<p>Dear HR Team,</p>" +
                    "<p>Please confirm the registration of Dr. " + doctorName +
                    " (License: " + doctorLicenceNumber + ")</p>" + "field" + doctorSpeciality +
                    "<form action=\"" + confirmationLink + "\" method=\"POST\">" +
                    "<button type=\"submit\" style=\"background-color: #4CAF50; color: white; " +
                    "padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;\">" +
                    "Confirm Registration</button>" +
                    "</form>" +
                    "</body></html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException("Failed to send confirmation email", e);
        }
    }

    private String createEmailContent(String doctorName, String licenceNumber, String confirmationLink) {
        return String.format(
                "Dear HR,\n\n" +
                        "Please confirm the registration of Dr. %s with license number %s.\n\n" +
                        "Click the link below to confirm:\n%s\n\n" +
                        "Thank you,\n" +
                        "Aleithia Team",
                doctorName, licenceNumber, confirmationLink
        );
    }
}

