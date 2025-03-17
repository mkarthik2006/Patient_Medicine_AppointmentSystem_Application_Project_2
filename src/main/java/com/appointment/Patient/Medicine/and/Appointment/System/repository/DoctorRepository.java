package com.appointment.Patient.Medicine.and.Appointment.System.repository;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
}