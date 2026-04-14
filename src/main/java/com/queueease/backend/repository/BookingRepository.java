package com.queueease.backend.repository;

import com.queueease.backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdAndCenterId(Long userId, Long centerId);

    List<Booking> findByUserId(Long userId); // ✅ ADD THIS

    Optional<Booking> findByCenterIdAndQueueNumber(Long centerId, Integer queueNumber);

    List<Booking> findByCenterIdOrderByQueueNumberAsc(Long centerId);
}