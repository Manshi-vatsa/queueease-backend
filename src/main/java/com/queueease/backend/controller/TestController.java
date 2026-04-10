package com.queueease.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")   // ✅ change here
    public String test() {
        return "Working 🚀";
    }
}