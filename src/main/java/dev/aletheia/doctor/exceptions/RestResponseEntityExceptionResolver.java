package dev.aletheia.doctor.exceptions;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionResolver extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { NotFoundException.class, DoctorNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(
            NotFoundException ex, WebRequest request) {
        return handleExceptionDefault(ex, HttpStatus.NOT_FOUND, request);
    }

    private ResponseEntity<Object> handleExceptionDefault(Exception ex, HttpStatus status, WebRequest request) {
        JSONObject response = new JSONObject();
        response.put("message", ex.getMessage());

        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }
}
