package dev.aletheia.doctor.emailservice;

// Custom exception class (create this in your exceptions package)
public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
