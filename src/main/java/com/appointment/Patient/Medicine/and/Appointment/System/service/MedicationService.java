package com.appointment.Patient.Medicine.and.Appointment.System.service;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Medication;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.DoctorRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.MedicationRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    public Medication createMedication(Long patientId,
                                       Long doctorId,
                                       String name,
                                       String dosage,
                                       String frequency,
                                       String duration) {
        // 1) Lookup Patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));

        // 2) If doctorId is provided, look up the doctor; else set to null
        Doctor doctor = null;
        if (doctorId != null) {
            doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        }

        // 3) Create the Medication entity
        Medication med = new Medication();
        med.setPatient(patient);
        med.setPrescribingDoctor(doctor);
        med.setName(name);
        med.setDosage(dosage);
        med.setFrequency(frequency);
        med.setDuration(duration);

        // 4) Save to DB
        return medicationRepository.save(med);
    }

    public List<Medication> getMedicationsForPatient(Long patientId) {
        return medicationRepository.findByPatientId(patientId);
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Medication getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found: " + id));
    }

    public Medication updateMedication(Long id,
                                       String dosage,
                                       String frequency,
                                       String duration) {
        Medication med = getMedicationById(id);
        med.setDosage(dosage);
        med.setFrequency(frequency);
        med.setDuration(duration);
        return medicationRepository.save(med);
    }

    public void deleteMedication(Long id) {

        medicationRepository.deleteById(id);
    }
}
