package com.appointment.Patient.Medicine.and.Appointment.System.service;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentService appointmentService;

    public Patient createPatient(User user, String fullName, String phone, String gender, String medicalHistory) {
        Patient patient = new Patient();
        patient.setFullName(fullName);
        patient.setPhone(phone);
        patient.setGender(gender);
        patient.setMedicalHistory(medicalHistory);
        patient.setUser(user);
        return patientRepository.save(patient);
    }

    public Patient getPatientByUser(User user) {
        return patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient not found for user: " + user.getEmail()));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
    }

    public Patient updatePatient(Long id, String fullName, String phone, String gender, String history) {
        Patient p = getPatientById(id);
        p.setFullName(fullName);
        p.setPhone(phone);
        p.setGender(gender);
        p.setMedicalHistory(history);
        return patientRepository.save(p);
    }

    public void deletePatient(Long id) {
        // 1) Check if this patient has any appointments
        List<Appointment> appts = appointmentService.getAppointmentsForPatient(id);
        if (!appts.isEmpty()) {
            // 2) Throw an exception if there are appointments
            throw new RuntimeException("Cannot delete patient with existing appointments. " +
                    "Please remove or reassign them first.");
        }

        // 3) Otherwise, proceed with deletion
        patientRepository.deleteById(id);
    }
}
