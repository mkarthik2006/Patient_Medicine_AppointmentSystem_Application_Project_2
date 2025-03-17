package com.appointment.Patient.Medicine.and.Appointment.System.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorCreationDto {
    private String fullName;
    private String email;     // Required for login
    private String password;  // Required for login
    private String specialization;
    private String phone;
}
