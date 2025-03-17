package com.appointment.Patient.Medicine.and.Appointment.System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleAlreadyExists(ResourceAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGlobalException(Exception ex) {
        return "An unexpected error occurred: " + ex.getMessage();
    }
}
