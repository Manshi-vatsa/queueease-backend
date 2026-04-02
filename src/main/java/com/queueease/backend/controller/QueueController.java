package com.queueease.backend.controller;

import com.queueease.backend.repository.QueueRepository;
import com.queueease.backend.model.Queue;
import com.queueease.backend.model.Booking;
import com.queueease.backend.service.BookingService;
import com.queueease.backend.dto.BookingResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final BookingService bookingService;
    private final QueueRepository queueRepository;

    public QueueController(BookingService bookingService, QueueRepository queueRepository) {
        this.bookingService = bookingService;
        this.queueRepository = queueRepository;
    }

    // ✅ JOIN QUEUE (CORRECT)
    @PostMapping("/joinQueue")
    public BookingResponse joinQueue(@RequestParam Long userId,
                                     @RequestParam Long centerId) {
        return bookingService.joinQueue(userId, centerId);
    }

    // ✅ SERVE NEXT
    @PostMapping("/serveNext/{centerId}")
    public Queue serveNext(@PathVariable Long centerId) {

        Queue queue = queueRepository.findByCenterId(centerId)
                .orElseThrow(() -> new RuntimeException("Queue not found"));

        queue.setCurrentServingNumber(queue.getCurrentServingNumber() + 1);

        return queueRepository.save(queue);
    }

    // ✅ USER BOOKINGS
    @GetMapping("/queueStatus/{userId}")
    public List<Booking> getQueueStatus(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    // ✅ CURRENT QUEUE
    @GetMapping("/current/{centerId}")
    public Queue getCurrentQueue(@PathVariable Long centerId) {
        return queueRepository.findByCenterId(centerId)
                .orElseThrow(() -> new RuntimeException("Queue not found"));
    }
}