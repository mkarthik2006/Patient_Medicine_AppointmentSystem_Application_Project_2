package com.appointment.Patient.Medicine.and.Appointment.System.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for patient data transfer.
 */
@Getter
@Setter
public class PatientDto {

    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;
    private String gender;
    private String medicalHistory;

    private Long userId;
}
