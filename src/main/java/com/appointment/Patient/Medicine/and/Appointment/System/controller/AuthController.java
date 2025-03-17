package com.appointment.Patient.Medicine.and.Appointment.System.controller;

import com.appointment.Patient.Medicine.and.Appointment.System.dto.AdminRegistrationDto;
import com.appointment.Patient.Medicine.and.Appointment.System.dto.RegistrationDto;
import com.appointment.Patient.Medicine.and.Appointment.System.model.Role;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.UserRepository;
import com.appointment.Patient.Medicine.and.Appointment.System.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        // Always enable the "Register as Admin" link
        model.addAttribute("enableAdminRegistration", true);
        return "login"; // Renders login.html
    }
    @GetMapping("/dashboard")
    public String postLoginRedirect(Authentication auth) {
        if (auth == null) {
            // Not logged in; redirect to login
            return "redirect:/login";
        }

        // Retrieve the user by email (username = email in Spring Security)
        User user = userService.getUserByEmail(auth.getName());

        // Route user to the correct dashboard based on role
        switch (user.getRole()) {
            case ADMIN:
                return "redirect:/admin/dashboard";
            case DOCTOR:
                return "redirect:/doctor/dashboard";
            case PATIENT:
            default:
                return "redirect:/patient/dashboard";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(@RequestParam(name = "role", required = false, defaultValue = "PATIENT") String role,
                                   Model model) {
        // Create a RegistrationDto, set the chosen role
        RegistrationDto dto = new RegistrationDto();
        dto.setRole(role); // "ADMIN", "DOCTOR", "PATIENT"
        model.addAttribute("userData", dto);
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("userData") RegistrationDto dto,
                                  BindingResult result,
                                  Model model) {
        // If validation fails, return the same page
        if (result.hasErrors()) {
            return "register";
        }

        // Convert string role to enum
        Role roleEnum = Role.valueOf(dto.getRole().toUpperCase());

        // Create the user with the chosen role
        User createdUser = userService.registerUser(dto.getName(), dto.getEmail(), dto.getPassword(), roleEnum);

        // If role is DOCTOR or PATIENT, create the corresponding entity
        switch (roleEnum) {
            case PATIENT:
                patientService.createPatient(
                        createdUser,
                        dto.getName(),
                        dto.getPhone(),
                        dto.getGender(),
                        dto.getMedicalHistory()
                );
                break;
            case DOCTOR:
                doctorService.createDoctorManually(
                        createdUser,
                        dto.getName(),
                        dto.getPhone(),
                        dto.getGender(),
                        dto.getSpecialization()
                );
                break;
            case ADMIN:

                break;
        }

        // Redirect to login (or show success message)
        model.addAttribute("success", "Account created successfully. Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/register-admin")
    public String registerAdminPage(Model model) {

        model.addAttribute("adminForm", new AdminRegistrationDto());
        return "register_admin"; // Renders register_admin.html
    }

    @PostMapping("/register-admin")
    public String registerAdmin(@Valid @ModelAttribute("adminForm") AdminRegistrationDto dto,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            return "register_admin";
        }

        // Create user with role = ADMIN
        userService.registerUser(dto.getName(), dto.getEmail(), dto.getPassword(), Role.ADMIN);

        model.addAttribute("success", "Admin account created. Please log in.");
        return "redirect:/login";
    }
}
