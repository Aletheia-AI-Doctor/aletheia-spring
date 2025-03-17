package dev.aletheia.doctor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String model, int defaultId) {
        super(model + " with id " + defaultId + " not found");
    }


    public NotFoundException() {
        super("Resource not found");
    }
}
