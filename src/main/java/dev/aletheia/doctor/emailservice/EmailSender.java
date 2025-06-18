package dev.aletheia.doctor.emailservice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from_address}")
    private String fromAddress;
    @Value("${spring.mail.from_name}")
    private String fromName;

    @Autowired
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendConfirmationRequest(String hrEmail, String doctorName, String doctorSpeciality,
                                        String doctorLicenceNumber,
                                        String confirmationLink,
                                        String rejectionLink)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, fromName);
        helper.setTo(hrEmail);
        helper.setSubject("Doctor Registration Confirmation: " + doctorName);

        String htmlContent = "<html><body style=\"font-family: Arial, sans-serif;\">" +
                "<p>Dear HR Team,</p>" +
                "<p>Please confirm the registration of Dr. " + doctorName +
                " (License: " + doctorLicenceNumber + ", Field: " + doctorSpeciality + ")</p>" +
                "<p>" +
                "<a href=\"" + confirmationLink + "\" style=\"" + getButtonStyle("#4CAF50") + "\">" +
                "Confirm Registration</a>" +
                "&nbsp;&nbsp;" +
                "<a href=\"" + rejectionLink + "\" style=\"" + getButtonStyle("#f44336") + "\">" +
                "Reject Registration</a>" +
                "</p>" +
                "</body></html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    @Async
    public void sendConfirmationDoctor(String doctorEmail, String doctorName,
                                       String hospitalName, String loginLink)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, fromName);

        helper.setTo(doctorEmail);
        helper.setSubject("Doctor Registration Confirmation: " + doctorName);

        String htmlContent = "<html><body style=\"font-family: Arial, sans-serif;\">" +
                "<p>Dear Dr. " + doctorName + ",</p>" +
                "<p>Your registration has been confirmed by " + hospitalName + " hospital.</p>" +
                "<p>Click the link below to login:</p>" +
                "<p>" +
                "<a href=\"" + loginLink + "\" style=\"" + getButtonStyle("#4CAF50") + "\">" +
                "Login Now</a>" +
                "</p>" +
                "</body></html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    @Async
    public void sendRejectionDoctor(String doctorEmail, String doctorName,
                                    String hospitalName, String appealLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, fromName);

        helper.setTo(doctorEmail);
        helper.setSubject("Doctor Registration Rejected: " + doctorName);

        String htmlContent = "<html><body style=\"font-family: Arial, sans-serif;\">" +
                "<p>Dear Dr. " + doctorName + ",</p>" +
                "<p>We regret to inform you that " + hospitalName + " hospital " +
                "has rejected your registration request.</p>" +
                "<p>You may click below to appeal this decision:</p>" +
                "<p>" +
                "<a href=\"" + appealLink + "\" style=\"" + getButtonStyle("#4CAF50") + "\">" +
                "Submit Appeal</a>" +
                "</p>" +
                "</body></html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private String getButtonStyle(String bgColor) {
        return "display: inline-block; " +
                "background-color: " + bgColor + "; " +
                "color: white; " +
                "padding: 10px 20px; " +
                "text-align: center; " +
                "text-decoration: none; " +
                "border-radius: 5px; " +
                "font-weight: bold; " +
                "cursor: pointer;";
    }
}