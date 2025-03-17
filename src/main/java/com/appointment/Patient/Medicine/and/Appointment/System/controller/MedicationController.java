package com.appointment.Patient.Medicine.and.Appointment.System.controller;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Medication;
import com.appointment.Patient.Medicine.and.Appointment.System.service.MedicationService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.PatientService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.UserService;
import com.appointment.Patient.Medicine.and.Appointment.System.service.DoctorService;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Patient;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;
    private final PatientService patientService;
    private final UserService userService;
    private final DoctorService doctorService;

    @Autowired
    public MedicationController(MedicationService medicationService,
                                PatientService patientService,
                                UserService userService,
                                DoctorService doctorService) {
        this.medicationService = medicationService;
        this.patientService = patientService;
        this.userService = userService;
        this.doctorService = doctorService; // <-- store doctorService
    }

    // 1) List medications (role-based)
    @GetMapping
    public String listMedications(Authentication auth, Model model) {
        User currentUser = userService.getUserByEmail(auth.getName());
        List<Medication> medications;

        switch (currentUser.getRole()) {
            case ADMIN:
                // Admin sees all medications
                medications = medicationService.getAllMedications();
                break;
            case PATIENT:
                // Patient sees only their meds
                Patient patient = patientService.getPatientByUser(currentUser);
                medications = medicationService.getMedicationsForPatient(patient.getId());
                break;
            default:

                medications = List.of();
                break;
        }

        model.addAttribute("medications", medications);
        return "medications_list"; // your listing page
    }

    // Show form to add medication
    @GetMapping("/new")
    public String showMedicationForm(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            Model model
    ) {
        // Provide a blank medication object
        model.addAttribute("medication", new Medication());

        // Provide full lists for dropdowns
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());

        // If you want to pre-select a patient or doctor in the form, pass them as well
        model.addAttribute("patientId", patientId);
        model.addAttribute("doctorId", doctorId);

        return "medications_form";
    }

    // Create medication
    @PostMapping
    public String createMedication(@RequestParam Long patientId,
                                   @RequestParam(required = false) Long doctorId,
                                   @RequestParam String name,
                                   @RequestParam String dosage,
                                   @RequestParam String frequency,
                                   @RequestParam String duration) {
        // Create medication in the service
        medicationService.createMedication(patientId, doctorId, name, dosage, frequency, duration);
        return "redirect:/medications";
    }

    // Edit medication form
    @GetMapping("/{id}/edit")
    public String editMedicationForm(@PathVariable Long id, Model model) {
        Medication med = medicationService.getMedicationById(id);
        model.addAttribute("medication", med);

        // If you also want to allow changing patient/doctor on edit, add the lists:
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());

        return "medications_form";
    }

    // Update medication
    @PostMapping("/{id}")
    public String updateMedication(@PathVariable Long id,
                                   @RequestParam String dosage,
                                   @RequestParam String frequency,
                                   @RequestParam String duration) {
        medicationService.updateMedication(id, dosage, frequency, duration);
        return "redirect:/medications";
    }

    // Delete medication
    @PostMapping("/{id}/delete")
    public String deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return "redirect:/medications";
    }
}

