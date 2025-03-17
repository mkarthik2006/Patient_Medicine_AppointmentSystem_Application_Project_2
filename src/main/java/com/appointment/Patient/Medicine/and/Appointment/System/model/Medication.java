package com.appointment.Patient.Medicine.and.Appointment.System.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medications")
@Getter
@Setter
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dosage;
    private String frequency;
    private String duration;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Optional: If you want to link prescribing doctor
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor prescribingDoctor;
}
