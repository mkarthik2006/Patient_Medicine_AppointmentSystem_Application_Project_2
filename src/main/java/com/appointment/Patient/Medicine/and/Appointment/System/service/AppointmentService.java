package com.appointment.Patient.Medicine.and.Appointment.System.service;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.AppointmentRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.DoctorRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    // For ADMIN
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Create new appointment
    public Appointment createAppointment(Long doctorId, Long patientId, LocalDateTime dateTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));

        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setDateTime(dateTime);
        appt.setStatus("SCHEDULED");
        return appointmentRepository.save(appt);
    }

    // For edit form
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + id));
    }

    // Doctor's appointments
    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Patient's appointments
    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // Update appointment with new doctor/patient/dateTime/status
    public Appointment updateAppointment(Long id,
                                         Long doctorId,
                                         Long patientId,
                                         LocalDateTime dateTime,
                                         String status) {
        Appointment appt = getAppointmentById(id);

        // Look up new doctor/patient
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));

        // Update fields
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setDateTime(dateTime);
        appt.setStatus(status);

        return appointmentRepository.save(appt);
    }

    // Delete
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}


