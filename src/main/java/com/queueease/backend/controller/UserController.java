package com.queueease.backend.controller;

import com.queueease.backend.model.User;
import com.queueease.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Optional<User> existingUser =
                userService.login(user.getEmail(), user.getPassword());

        if(existingUser.isPresent()) {
            return "Login Successful";
        }

        return "Invalid email or password";
    }
}