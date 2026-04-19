package com.queueease.backend.repository;

import com.queueease.backend.model.CenterResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCenterRepository extends JpaRepository<CenterResponse, Long> {
}