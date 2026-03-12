package com.queueease.backend.controller;

import com.queueease.backend.model.User;
import com.queueease.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // REGISTER USER
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.register(user);
    }

    // LOGIN USER
    @PostMapping("/login")
public String loginUser(@RequestBody User user) {

    Optional<User> existingUser =
            userService.login(user.getEmail(), user.getPassword());

    if(existingUser.isPresent()) {
        return "Login Successful";
    } else {
        return "Invalid email or password";
    }
}
}