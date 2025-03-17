package com.appointment.Patient.Medicine.and.Appointment.System.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Role;


@Getter
@Setter
public class UserDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private Role role; // ADMIN, DOCTOR, PATIENT
}
