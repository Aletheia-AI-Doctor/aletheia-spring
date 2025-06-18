package dev.aletheia.doctor.emailservice;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Value("${spring.mail.from_address}")
    private String fromAddress;
    @Value("${spring.mail.from_name}")
    private String fromName;

    @Value("${aws.region}")
    private String awsRegion;


    public void sendConfirmationRequest(
            String hrEmail,
            String doctorName,
            String doctorSpeciality,
            String doctorLicenceNumber,
            String confirmationLink,
            String rejectionLink
    ) {
        String subject = "Doctor Registration Confirmation: " + doctorName;
        String htmlBody = "<html><body style=\"font-family:Arial,sans-serif;\">"
                + "<p>Dear HR Team,</p>"
                + "<p>Please confirm the registration of Dr. " + doctorName
                + " (License: " + doctorLicenceNumber + ", Field: " + doctorSpeciality + ")</p>"
                + "<p>"
                + actionButton(confirmationLink, "Confirm Registration", "#4CAF50")
                + "&nbsp;&nbsp;"
                + actionButton(rejectionLink, "Reject Registration", "#f44336")
                + "</p></body></html>";

        send(hrEmail, subject, htmlBody);
    }

    public void sendConfirmationDoctor(
            String doctorEmail,
            String doctorName,
            String hospitalName,
            String loginLink
    ) {
        String subject = "Doctor Registration Confirmation: " + doctorName;
        String htmlBody = "<html><body style=\"font-family:Arial,sans-serif;\">"
                + "<p>Dear Dr. " + doctorName + ",</p>"
                + "<p>Your registration has been confirmed by " + hospitalName + " hospital.</p>"
                + "<p>Click the link below to login:</p>"
                + "<p>" + actionButton(loginLink, "Login Now", "#4CAF50") + "</p>"
                + "</body></html>";

        send(doctorEmail, subject, htmlBody);
    }

    public void sendRejectionDoctor(
            String doctorEmail,
            String doctorName,
            String hospitalName,
            String appealLink
    ) {
        String subject = "Doctor Registration Rejected: " + doctorName;
        String htmlBody = "<html><body style=\"font-family:Arial,sans-serif;\">"
                + "<p>Dear Dr. " + doctorName + ",</p>"
                + "<p>We regret to inform you that " + hospitalName + " hospital has rejected your registration request.</p>"
                + "<p>You may click below to appeal this decision:</p>"
                + "<p>" + actionButton(appealLink, "Submit Appeal", "#4CAF50") + "</p>"
                + "</body></html>";

        send(doctorEmail, subject, htmlBody);
    }

    private void send(String to, String subject, String htmlBody) {
        SendEmailRequest request = new SendEmailRequest().withDestination(
                        new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(htmlBody)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(subject)))
                .withSource(fromAddress);

        AmazonSimpleEmailService ses = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(awsRegion)
                .build();

        ses.sendEmail(request);
    }

    private String formatFromHeader() {
        return String.format("\"%s\" <%s>", fromName, fromAddress);
    }

    private String actionButton(String url, String label, String bgColor) {
        return "<a href=\"" + url + "\" style=\""
                + "display:inline-block;"
                + "background-color:" + bgColor + ";"
                + "color:white;"
                + "padding:10px 20px;"
                + "text-decoration:none;"
                + "border-radius:5px;"
                + "font-weight:bold;\">"
                + label + "</a>";
    }
}
