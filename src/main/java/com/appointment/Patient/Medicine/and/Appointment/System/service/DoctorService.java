package com.appointment.Patient.Medicine.and.Appointment.System.service;

import com.appointment.Patient.Medicine.and.Appointment.System.dto.DoctorCreationDto;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Role;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserService userService;

    // 1) Add the AppointmentService to check for existing appointments
    @Autowired
    private AppointmentService appointmentService;

    public Doctor createDoctor(DoctorCreationDto dto) {
        // 1) Validate required fields for login
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank when creating a new doctor.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank when creating a new doctor.");
        }

        // 2) Register the user with role = DOCTOR
        User user = userService.registerUser(
                dto.getFullName(),     // Use full name for "name" field
                dto.getEmail(),
                dto.getPassword(),
                Role.DOCTOR
        );

        // 3) Create the Doctor entity and link to the newly created user
        Doctor doctor = new Doctor();
        doctor.setFullName(dto.getFullName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setPhone(dto.getPhone());
        doctor.setUser(user);

        // 4) Save to DB
        return doctorRepository.save(doctor);
    }

    public Doctor createDoctorManually(User user,
                                       String fullName,
                                       String phone,
                                       String gender,
                                       String specialization) {
        // Optionally check that the user is actually a DOCTOR
        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException("User is not assigned the DOCTOR role.");
        }

        Doctor doctor = new Doctor();
        doctor.setFullName(fullName);
        doctor.setPhone(phone);
        doctor.setSpecialization(specialization);
        doctor.setUser(user);

        return doctorRepository.save(doctor);
    }

    public Doctor getDoctorByUser(User user) {
        return doctorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor not found for user: " + user.getEmail()));
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
    }

    public Doctor updateDoctor(Long id, String fullName, String specialization, String phone) {
        Doctor d = getDoctorById(id);
        d.setFullName(fullName);
        d.setSpecialization(specialization);
        d.setPhone(phone);
        return doctorRepository.save(d);
    }

    public void deleteDoctor(Long id) {
        // 1) Check if the doctor has existing appointments
        List<Appointment> appts = appointmentService.getAppointmentsForDoctor(id);
        if (!appts.isEmpty()) {
            // 2) Throw an exception or handle as needed
            throw new RuntimeException("Cannot delete doctor with existing appointments. " +
                    "Please remove or reassign them first.");
        }

        // 3) If no appointments, proceed with deletion
        doctorRepository.deleteById(id);
    }
}
