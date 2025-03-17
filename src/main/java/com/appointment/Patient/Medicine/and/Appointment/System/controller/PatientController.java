package com.appointment.Patient.Medicine.and.Appointment.System.controller;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Medication;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private UserService userService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private MedicationService medicationService;

    @GetMapping("/dashboard")
    public String patientDashboard(Authentication auth, Model model) {
        // 1) Get user from DB
        User user = userService.getUserByEmail(auth.getName());

        // 2) Get patient entity
        Patient patient = patientService.getPatientByUser(user);

        // 3) Retrieve appointments & medications
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patient.getId());
        List<Medication> medications = medicationService.getMedicationsForPatient(patient.getId());

        // 4) Populate model for Thymeleaf
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointments);
        model.addAttribute("medications", medications);

        return "patient_dashboard";
    }

    // Book Appointment (patient can pick doctor and date/time)
    @GetMapping("/book-appointment")
    public String bookAppointmentForm(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "book_appointment";
    }

    @PostMapping("/book-appointment")
    public String bookAppointment(@RequestParam Long doctorId,
                                  @RequestParam String dateTime,
                                  Authentication auth) {
        // 1) Identify the patient from the current user
        User user = userService.getUserByEmail(auth.getName());
        Patient patient = patientService.getPatientByUser(user);

        // 2) Parse date/time
        LocalDateTime dt = LocalDateTime.parse(dateTime);

        // 3) Create the appointment
        appointmentService.createAppointment(doctorId, patient.getId(), dt);

        // 4) Redirect back to patient dashboard
        return "redirect:/patient/dashboard";
    }
    @GetMapping("/edit-profile")
    public String editProfileForm(Authentication auth, Model model) {
        // 1) Identify the current user
        User user = userService.getUserByEmail(auth.getName());

        // 2) Retrieve the patient entity
        Patient patient = patientService.getPatientByUser(user);

        // 3) Add the patient to the model for the form
        model.addAttribute("patient", patient);

        // 4) Render a Thymeleaf template named "patient_profile_edit.html"
        return "patient_profile_edit";
    }
    @PostMapping("/update-profile")
    public String updateProfile(Authentication auth,
                                @RequestParam String fullName,
                                @RequestParam String phone,
                                @RequestParam String gender,
                                @RequestParam String medicalHistory) {
        // 1) Identify the current user
        User user = userService.getUserByEmail(auth.getName());

        // 2) Retrieve the patient
        Patient patient = patientService.getPatientByUser(user);

        // 3) Update patient record
        patientService.updatePatient(patient.getId(), fullName, phone, gender, medicalHistory);

        // 4) Redirect back to dashboard (or show success message)
        return "redirect:/patient/dashboard";
    }
}


