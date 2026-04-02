package com.queueease.backend.repository;
import java.util.Optional;
import com.queueease.backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
   List<Booking> findByUserIdAndCenterId(Long userId, Long centerId);
    List<Booking> findByUserId(Long userId);
}
