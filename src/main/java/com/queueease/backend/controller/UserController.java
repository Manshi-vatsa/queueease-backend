package com.queueease.backend.controller;

import com.queueease.backend.model.User;
import com.queueease.backend.service.UserService;
import com.queueease.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.login(user.getEmail(), user.getPassword());

        if (existingUser.isPresent()) {
            // Generate token using the email from the database
            String token = jwtUtil.generateToken(existingUser.get().getEmail());
            return ResponseEntity.ok(Map.of(
                "message", "Login Successfully",
                "token", token
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                "message", "Invalid email or password"
            ));
        }
    }
}