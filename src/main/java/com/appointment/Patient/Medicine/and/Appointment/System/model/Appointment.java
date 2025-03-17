package com.appointment.Patient.Medicine.and.Appointment.System.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;
    private String status; // SCHEDULED, CANCELLED, COMPLETED

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
}
