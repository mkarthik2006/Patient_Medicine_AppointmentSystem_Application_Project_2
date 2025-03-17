package com.appointment.Patient.Medicine.and.Appointment.System.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}