package dev.aletheia.doctor.emailservice;

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

    private InternetAddress fromInternetAddress;

    @Autowired
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        try {
            this.fromInternetAddress = new InternetAddress(fromAddress, fromName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendConfirmationRequest(String hrEmail, String doctorName, String doctorSpeciality,
                                        String doctorLicenceNumber, String confirmationLink, String rejectionLink) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            message.setFrom(this.fromInternetAddress);
            helper.setFrom(this.fromInternetAddress);

            helper.setTo(hrEmail);
            helper.setSubject("Doctor Registration Confirmation: " + doctorName);

            String htmlContent = "<html><body>" +
                    "<p>Dear HR Team,</p>" +
                    "<p>Please confirm the registration of Dr. " + doctorName +
                    " (License: " + doctorLicenceNumber + ")</p>" + "field: " + doctorSpeciality +
                    "<form action=\"" + confirmationLink + "\" method=\"POST\">" +
                    "<button type=\"submit\" style=\"background-color: #4CAF50; color: white; " +
                    "padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;\">" +
                    "Confirm Registration</button>" +
                    "</form>" + "<p>     </p>"+
                    "<form action=\"" + rejectionLink + "\" method=\"POST\">" +
                    "<button type=\"submit\" style=\"background-color: #4CAF50; color: white; " +
                    "padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;\">" +
                    "Reject Registration</button>" +
                    "</form>" +
                    "</body></html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailSendingException("Failed to send confirmation email", e);
        }
    }

    @Async
    public void sendConfirmationDoctor(String doctorEmail, String doctorName,
                                        String hospitalName, String loginLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(this.fromInternetAddress);
            helper.setFrom(this.fromInternetAddress);

            helper.setTo(doctorEmail);
            helper.setSubject("Doctor Registration Confirmation: " + doctorName);

            String htmlContent = "<html><body>" +
                    "<p>Dear Dr.</p>" + doctorName + "," +
                    "<p>you have been confirmed by" + hospitalName +" hospital"+
                    "<p>Click the link below to login:</p>" +
                    "<form action=\"" + loginLink + "\" method=\"GET\">" +
                    "<button type=\"submit\" style=\"background-color: #4CAF50; color: white; " +
                    "padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;\">" +
                    "Login Now!</button>" +
                    "</form>" +
                    "</body></html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailSendingException("Failed to send confirmation email", e);
        }
    }

    @Async
    public void sendRejectionDoctor(String doctorEmail, String doctorName,
                                       String hospitalName, String appealLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(this.fromInternetAddress);
            helper.setFrom(this.fromInternetAddress);

            helper.setTo(doctorEmail);
            helper.setSubject("Doctor Registration rejected: " + doctorName);

            String htmlContent = "<html><body>" +
                    "<p>Dear Dr.</p>" + doctorName + "," +
                    "<p>We are unhappy to announce that" + hospitalName +" hospital"+
                    "<p>rejected to authenticate your account, you can click here to appeal</p>" +
                    "<form action=\"" + appealLink + "\" method=\"GET\">" +
                    "<button type=\"submit\" style=\"background-color: #4CAF50; color: white; " +
                    "padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;\">" +
                    "appeal</button>" +
                    "</form>" +
                    "</body></html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailSendingException("Failed to send confirmation email", e);
        }
    }
}

