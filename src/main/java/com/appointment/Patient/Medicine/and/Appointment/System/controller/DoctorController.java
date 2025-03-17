package com.appointment.Patient.Medicine.and.Appointment.System.controller;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.service.AppointmentService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.DoctorService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/dashboard")
    public String doctorDashboard(Authentication auth, Model model) {
        // 1) Retrieve the logged-in user
        User user = userService.getUserByEmail(auth.getName());

        // 2) Fetch the corresponding Doctor entity
        Doctor doctor = doctorService.getDoctorByUser(user);

        // 3) Retrieve appointments for this doctor
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctor.getId());

        // 4) Populate the model for the Thymeleaf template
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);

        return "doctor_dashboard"; // Renders the "doctor_dashboard.html" view
    }
}

