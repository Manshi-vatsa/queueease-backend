package com.queueease.backend.controller;

import com.queueease.backend.model.ServiceCenter;
import com.queueease.backend.service.ServiceCenterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/centers")
public class ServiceCenterController {

    private final ServiceCenterService service;

    public ServiceCenterController(ServiceCenterService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceCenter> getCenters() {
        return service.getAllCenters();
    }

    @PostMapping
    public ServiceCenter addCenter(@RequestBody ServiceCenter center) {
        return service.addCenter(center);
    }
}