package com.queueease.backend.service;

import com.queueease.backend.model.CenterResponse;
import com.queueease.backend.repository.ServiceCenterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCenterService {

    private final ServiceCenterRepository repository;

    public ServiceCenterService(ServiceCenterRepository repository) {
        this.repository = repository;
    }

    public List<CenterResponse> getAllCenters() {
        return repository.findAll();
    }

    public CenterResponse addCenter(CenterResponse center) {
        return repository.save(center);
    }
}