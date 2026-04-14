package com.queueease.backend.controller;

import com.queueease.backend.mongo.QueueActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final QueueActivityService queueActivityService;

    public AdminController(QueueActivityService queueActivityService) {
        this.queueActivityService = queueActivityService;
    }

    // ✅ ANALYTICS API
    @GetMapping("/analytics")
    public Map<String, Object> getAnalytics() {

        Map<String, Object> data = new HashMap<>();

        data.put("avgWaitTime", queueActivityService.getAverageWaitTime());
        data.put("peakHours", queueActivityService.getPeakHours());

        return data;
    }
}