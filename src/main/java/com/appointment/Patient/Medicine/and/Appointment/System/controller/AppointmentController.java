package com.appointment.Patient.Medicine.and.Appointment.System.controller;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Appointment;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Role;
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
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final UserService userService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                 PatientService patientService,
                                 DoctorService doctorService,
                                 UserService userService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.userService = userService;
    }

    // List all appointments (role-based)
    @GetMapping
    public String listAppointments(Authentication auth, Model model) {
        User currentUser = userService.getUserByEmail(auth.getName());
        List<Appointment> appointments;

        switch (currentUser.getRole()) {
            case ADMIN:
                // Admin sees all appointments
                appointments = appointmentService.getAllAppointments();
                break;
            case DOCTOR:
                // Doctor sees only their appointments
                Doctor doctor = doctorService.getDoctorByUser(currentUser);
                appointments = appointmentService.getAppointmentsForDoctor(doctor.getId());
                break;
            case PATIENT:
            default:
                // Patient sees only their appointments
                Patient patient = patientService.getPatientByUser(currentUser);
                appointments = appointmentService.getAppointmentsForPatient(patient.getId());
                break;
        }

        model.addAttribute("appointments", appointments);
        return "appointments_list";
    }

    // Show form to create a new appointment
    @GetMapping("/new")
    public String showCreateForm(Authentication auth, Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAllDoctors());

        // If user is ADMIN or DOCTOR, allow picking a patient from a dropdown
        User currentUser = userService.getUserByEmail(auth.getName());
        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.DOCTOR) {
            model.addAttribute("patients", patientService.getAllPatients());
        }

        return "appointments_form";
    }

    // Create / save appointment
    @PostMapping
    public String createAppointment(Authentication auth,
                                    @RequestParam Long doctorId,
                                    @RequestParam(required = false) Long patientId,
                                    @RequestParam String dateTime) {
        User currentUser = userService.getUserByEmail(auth.getName());
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);

        if (currentUser.getRole() == Role.PATIENT) {
            // Patient is creating an appointment for themselves
            Patient patient = patientService.getPatientByUser(currentUser);
            appointmentService.createAppointment(doctorId, patient.getId(), parsedDateTime);
        } else {
            // Admin or Doctor is creating an appointment for a chosen patient
            if (patientId == null) {
                throw new RuntimeException("Patient ID is required for Admin/Doctor to create an appointment.");
            }
            appointmentService.createAppointment(doctorId, patientId, parsedDateTime);
        }

        return "redirect:/appointments";
    }

    // Show form to edit an existing appointment
    @GetMapping("/{id}/edit")
    public String editAppointmentForm(@PathVariable Long id, Authentication auth, Model model) {
        Appointment appt = appointmentService.getAppointmentById(id);

        // Add the appointment, plus all doctors/patients for dropdowns
        model.addAttribute("appointment", appt);
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients());

        return "appointments_form";
    }

    //Update appointment (including patient, doctor, dateTime, and status)
    @PostMapping("/{id}")
    public String updateAppointment(@PathVariable Long id,
                                    @RequestParam Long doctorId,
                                    @RequestParam Long patientId,
                                    @RequestParam String dateTime,
                                    @RequestParam String status) {
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);

        // Let the service update doctor, patient, dateTime, and status
        appointmentService.updateAppointment(id, doctorId, patientId, parsedDateTime, status);

        return "redirect:/appointments";
    }

    //Delete / cancel appointment
    @PostMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}

