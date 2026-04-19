package com.queueease.backend.controller;

import com.queueease.backend.model.CenterResponse;
import com.queueease.backend.model.ServiceCenter;
import com.queueease.backend.service.ServiceCenterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centers")
public class ServiceCenterController {

    private final ServiceCenterService service;

    public ServiceCenterController(ServiceCenterService service) {
        this.service = service;
    }

    @GetMapping
    public List<CenterResponse> getCenters() {
        return service.getAllCenters();
    }

    @PostMapping
    public CenterResponse addCenter(@RequestBody CenterResponse center) {
        return service.addCenter(center);
    }
}