package com.appointment.Patient.Medicine.and.Appointment.System.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDto {

    private Long id; // for updates or returning from service

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment date/time is required")
    private LocalDateTime dateTime;

    private String status; //"SCHEDULED", "CANCELLED", "COMPLETED"
}
