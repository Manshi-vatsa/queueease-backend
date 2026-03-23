package com.queueease.backend.service;


import com.queueease.backend.model.Booking;
import com.queueease.backend.model.Queue;
import com.queueease.backend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final QueueService queueService;

    public BookingService(BookingRepository bookingRepository, QueueService queueService) {
        this.bookingRepository = bookingRepository;
        this.queueService = queueService;
    }

    public Booking joinQueue(Long userId, Long centerId) {
        Queue queue = queueService.incrementQueue(centerId);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCenterId(centerId);
        booking.setQueueNumber(queue.getCurrentServingNumber());

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}