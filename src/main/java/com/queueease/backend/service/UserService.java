package com.queueease.backend.service;

import com.queueease.backend.model.User;
import com.queueease.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ ADD THIS

    // ✅ FIXED REGISTER METHOD
    public User register(User user) {

        // normalize email
        user.setEmail(user.getEmail().trim().toLowerCase());

        // 🔥 IMPORTANT: encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // ✅ FIXED LOGIN METHOD
    public Optional<User> login(String email, String password) {

        // normalize email
        email = email.trim().toLowerCase();

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        // 🔥 IMPORTANT: use BCrypt match
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return Optional.of(user);
    }
}