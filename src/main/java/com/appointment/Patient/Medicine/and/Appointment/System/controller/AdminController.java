package com.appointment.Patient.Medicine.and.Appointment.System.controller;

import com.appointment.Patient.Medicine.and.Appointment.System.dto.DoctorCreationDto;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Doctor;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.service.DoctorService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin_dashboard";
    }

    // List all doctors
    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);

        // Provide a DTO for creating new doctors
        model.addAttribute("doctorForm", new DoctorCreationDto());
        return "admin_doctors";
    }

    // Create new doctor
    @PostMapping("/doctors")
    public String createDoctor(@ModelAttribute DoctorCreationDto doctorForm,
                               Model model) {
        try {
            // Attempt to create the doctor
            doctorService.createDoctor(doctorForm);
            // On success, redirect to the list
            return "redirect:/admin/doctors";
        } catch (RuntimeException ex) {
            // If something goes wrong (e.g., "Email already in use"), catch the exception
            model.addAttribute("errorMessage", ex.getMessage());
            // Re-populate the existing doctors so the page can display them
            model.addAttribute("doctors", doctorService.getAllDoctors());
            // Keep the form data so user doesn't have to re-enter everything
            model.addAttribute("doctorForm", doctorForm);
            // Return the same template to show the error message
            return "admin_doctors";
        }
    }

    // Show form to edit an existing doctor
    @GetMapping("/doctors/{id}/edit")
    public String editDoctorForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "admin_doctor_edit";
    }

    // Update doctor
    @PostMapping("/doctors/{id}")
    public String updateDoctor(@PathVariable Long id,
                               @RequestParam String fullName,
                               @RequestParam String specialization,
                               @RequestParam String phone) {
        doctorService.updateDoctor(id, fullName, specialization, phone);
        return "redirect:/admin/doctors";
    }

    // Delete doctor
    @PostMapping("/doctors/{id}/delete")
    public String deleteDoctor(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            doctorService.deleteDoctor(id);
            // If deletion succeeds
            redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully.");
        } catch (RuntimeException ex) {
            // If doctor has existing appointments or another runtime issue
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/doctors";
    }

    // List all patients
    @GetMapping("/patients")
    public String listPatients(Model model) {
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "admin_patients";
    }

    // Show form to edit a patient
    @GetMapping("/patients/{id}/edit")
    public String editPatientForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "admin_patient_edit";
    }

    // Update patient
    @PostMapping("/patients/{id}")
    public String updatePatient(@PathVariable Long id,
                                @RequestParam String fullName,
                                @RequestParam String phone,
                                @RequestParam String gender,
                                @RequestParam String medicalHistory) {
        patientService.updatePatient(id, fullName, phone, gender, medicalHistory);
        return "redirect:/admin/patients";
    }


    // Delete patient
    @PostMapping("/patients/{id}/delete")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patientService.deletePatient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Patient deleted successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/patients";
    }
}
