package com.queueease.backend.service;

import com.queueease.backend.model.Booking;
import com.queueease.backend.model.Queue;
import com.queueease.backend.repository.BookingRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.queueease.backend.dto.BookingResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final QueueService queueService;

    public BookingService(BookingRepository bookingRepository, QueueService queueService) {
        this.bookingRepository = bookingRepository;
        this.queueService = queueService;
    }

    public BookingResponse joinQueue(Long userId, Long centerId) {
        System.out.println("JOIN QUEUE CALLED");
        // ✅ Validation
        if (userId == null || centerId == null) {
            throw new RuntimeException("Invalid input");
        }

        // ✅ Duplicate check
        List<Booking> existingBookings =
                bookingRepository.findByUserIdAndCenterId(userId, centerId);

        if (!existingBookings.isEmpty()) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, "User already in queue");
        }

        // ✅ Get & increment queue
        Queue queue = queueService.incrementQueue(centerId);

        // 🔥 FIX: handle NULL currentNumber
        if (queue.getCurrentNumber() == null) {
            queue.setCurrentNumber(1);
        }

        int queueNumber = queue.getCurrentNumber();

        // ✅ Create booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCenterId(centerId);
        booking.setQueueNumber(queueNumber);

        Booking savedBooking = bookingRepository.save(booking);

        // ✅ Response
        BookingResponse response = new BookingResponse();
        response.setUserId(userId);
        response.setQueueNumber(savedBooking.getQueueNumber());
        response.setMessage("Successfully joined queue");

        return response;
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}