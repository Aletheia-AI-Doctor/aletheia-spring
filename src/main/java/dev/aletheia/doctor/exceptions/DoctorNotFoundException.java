package dev.aletheia.doctor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DoctorNotFoundException extends NotFoundException {
    public DoctorNotFoundException(String message) {
        super(message);
    }

    public DoctorNotFoundException() {
        super("Doctor not found");
    }
}
