package com.appointment.Patient.Medicine.and.Appointment.System.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;


    private String phone;
    private String gender;
    private String medicalHistory;


    private String specialization;

    // Role: "ADMIN", "DOCTOR", or "PATIENT"
    private String role;
}
