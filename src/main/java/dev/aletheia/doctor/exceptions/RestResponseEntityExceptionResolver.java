package dev.aletheia.doctor.exceptions;

import jakarta.validation.ConstraintViolationException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionResolver extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {NotFoundException.class, DoctorNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(
            NotFoundException ex, WebRequest request) {
        return handleExceptionDefault(ex, HttpStatus.NOT_FOUND, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            List<String> messages = new ArrayList<>(
                    errors.containsKey(error.getField())
                            ? errors.get(error.getField())
                            : List.of()
            );

            messages.add(error.getDefaultMessage());

            errors.put(error.getField(), messages);
        });

        JSONObject response = new JSONObject();
        response.put("message", "Validation error");
        response.put("errors", errors);

        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = {InvalidCredentialsException.class})
    protected ResponseEntity<Object> handleInvalidCreds(
            InvalidCredentialsException ex, WebRequest request) {
        return handleExceptionDefault(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value
            = {UnauthorizedException.class})
    protected ResponseEntity<Object> handleUnauthorized(
            UnauthorizedException ex, WebRequest request) {
        return handleExceptionDefault(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value
            = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidation(
            ConstraintViolationException ex, WebRequest request) {

        JSONObject response = new JSONObject();
        JSONObject errors = new JSONObject();

        ex.getConstraintViolations().forEach(violation -> {
            String key = violation.getPropertyPath().toString();
            JSONArray messages = errors.containsKey(key)
                    ? (JSONArray) errors.get(key)
                    : new JSONArray();
            messages.add(violation.getMessage());

            errors.put(key, messages);
        });

        response.put("message", errors.values().stream().findFirst().orElse("Validation error"));
        response.put("errors", errors);

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> handleExceptionDefault(Exception ex, HttpStatus status, WebRequest request) {
        JSONObject response = new JSONObject();
        response.put("message", ex.getMessage());

        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }
}
