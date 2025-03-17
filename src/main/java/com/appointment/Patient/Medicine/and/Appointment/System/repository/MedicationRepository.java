package com.appointment.Patient.Medicine.and.Appointment.System.repository;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByPatientId(Long patientId);
}
