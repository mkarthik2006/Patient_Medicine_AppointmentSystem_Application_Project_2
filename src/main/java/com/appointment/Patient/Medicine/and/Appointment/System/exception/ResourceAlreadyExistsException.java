package com.appointment.Patient.Medicine.and.Appointment.System.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}