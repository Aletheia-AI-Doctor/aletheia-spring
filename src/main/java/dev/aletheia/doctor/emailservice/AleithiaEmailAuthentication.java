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
                                        String doctorLicenceNumber, String confirmationLink, String rejectionLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

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
            System.out.println(confirmationLink);
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException("Failed to send confirmation email", e);
        }
    }

    public void sendConfirmationDoctor(String doctorEmail, String doctorName,
                                        String hospitalName, String loginLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

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
            throw new EmailSendingException("Failed to send confirmation email", e);
        }
    }
    public void sendRejectionDoctor(String doctorEmail, String doctorName,
                                       String hospitalName, String appealLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

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
            throw new EmailSendingException("Failed to send confirmation email", e);
        }
    }
}

