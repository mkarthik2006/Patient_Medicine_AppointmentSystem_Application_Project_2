package com.appointment.Patient.Medicine.and.Appointment.System.controller;

import com.appointment.Patient.Medicine.and.Appointment.System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordController {

    @Autowired
    private UserService userService;


    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        return "forgot_password"; // Renders forgot_password.html
    }


    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        Model model) {
        // Check if a user with that email exists
        boolean userExists = userService.checkIfUserExists(email);
        if (!userExists) {
            model.addAttribute("error", "No user found with that email.");
            return "forgot_password";
        }

        // Generate/store the token, optionally send email with link
        String token = userService.initiatePasswordReset(email);

        // Show success message
        model.addAttribute("success", "A reset link has been sent to your email (if it exists).");
        return "forgot_password";
    }

    // Show the reset form
    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("token") String token,
                                @RequestParam(value = "email", required = false) String email,
                                Model model) {
        // Validate the token
        boolean valid = userService.isResetTokenValid(token);
        if (!valid) {
            model.addAttribute("error", "Invalid or expired token");
            return "forgot_password"; // Or show an error page
        }

        // Put token and email in the model so the form can use them
        model.addAttribute("token", token);
        model.addAttribute("email", email);

        return "reset_password"; // Renders reset_password.html
    }

    // 4) Handle the final password reset
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("email") String email,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       Model model) {

        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return "reset_password";
        }

        // Actually update the password in DB
        try {
            userService.updatePasswordByToken(token, newPassword);
            model.addAttribute("success", "Password updated successfully. Please log in.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return "reset_password";
        }
    }
}
