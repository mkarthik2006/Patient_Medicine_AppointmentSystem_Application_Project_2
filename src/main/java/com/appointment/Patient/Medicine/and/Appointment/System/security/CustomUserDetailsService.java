package com.appointment.Patient.Medicine.and.Appointment.System.security;

import com.appointment.Patient.Medicine.and.Appointment.System.model.User;
import com.appointment.Patient.Medicine.and.Appointment.System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve user entity from DB or throw exception if not found
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Convert the single role to a Spring Security authority (ROLE_<roleName>)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        // Return a Spring Security UserDetails object with the user's credentials and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(authority)
        );
    }
}
