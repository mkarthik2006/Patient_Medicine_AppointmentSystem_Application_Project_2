package com.appointment.Patient.Medicine.and.Appointment.System.service;

import com.appointment.Patient.Medicine.and.Appointment.System.model.Role;
import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // For registering new users
    public User registerUser(String name, String email, String rawPassword, Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use: " + email);
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        return userRepository.save(user);
    }

    // Retrieve user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    // 1) Check if user with the given email exists
    public boolean checkIfUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // 2) Generate and store the token
    public String initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        String token = UUID.randomUUID().toString();

        // Store the token + expiry in the user record
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        return token;
    }

    // 3) Validate the token
    public boolean isResetTokenValid(String token) {
        Optional<User> userOpt = userRepository.findByResetToken(token);
        if (userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();

        LocalDateTime expiry = user.getResetTokenExpiry();
        if (expiry == null || expiry.isBefore(LocalDateTime.now())) {
            return false; // expired
        }
        return true;
    }

    // 4) Update password by token
    public void updatePasswordByToken(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        // Encode the new password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear token so it can't be reused
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }
}
