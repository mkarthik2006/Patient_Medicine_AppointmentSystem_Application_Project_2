package com.appointment.Patient.Medicine.and.Appointment.System.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "doctors")
@Getter
@Setter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    private String specialization;
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}

