package com.queueease.backend.controller;

import com.queueease.backend.model.User;
import com.queueease.backend.service.UserService;
import com.queueease.backend.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        // 🔒 Ensure role is never null
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        User savedUser = userService.register(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("userId", savedUser.getId());

        return ResponseEntity.ok(response);
    }

    // ✅ LOGIN (FULLY FIXED)
    @PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody User user) {

    Optional<User> existingUser = userService.login(user.getEmail(), user.getPassword());

    if (existingUser.isPresent()) {

        User dbUser = existingUser.get();

        String token = jwtUtil.generateToken(dbUser.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login Successfully");
        response.put("token", token);
        response.put("userId", dbUser.getId());   // ✅ IMPORTANT
        response.put("role", dbUser.getRole());

        return ResponseEntity.ok(response);

    } else {
        return ResponseEntity.status(401).body(Map.of(
                "message", "Invalid email or password"
        ));
    }
}
    }
