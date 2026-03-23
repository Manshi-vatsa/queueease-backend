package com.queueease.backend.repository;



import com.queueease.backend.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    Optional<Queue> findByCenterId(Long centerId);
}