package com.appointment.Patient.Medicine.and.Appointment.System.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for transferring medication data.
 */
@Getter
@Setter
public class MedicationDto {

    private Long id; // for updates or returning from service

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    // If you track which doctor prescribed it
    private Long prescribingDoctorId;

    @NotBlank(message = "Medication name is required")
    private String name;

    private String dosage;    // e.g. "500 mg"
    private String frequency; // e.g. "twice a day"
    private String duration;  // e.g. "1 week"
}
