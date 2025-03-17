package com.appointment.Patient.Medicine.and.Appointment.System.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patients")
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    private String phone;
    private String gender;
    private String medicalHistory;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
