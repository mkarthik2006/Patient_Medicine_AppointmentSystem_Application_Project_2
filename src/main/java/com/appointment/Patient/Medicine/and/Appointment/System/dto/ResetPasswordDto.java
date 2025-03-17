package com.appointment.Patient.Medicine.and.Appointment.System.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    private String email;
    private String newPassword;
    private String confirmPassword;
}