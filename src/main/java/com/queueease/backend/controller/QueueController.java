package com.queueease.backend.controller;


import com.queueease.backend.model.Booking;
import com.queueease.backend.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final BookingService bookingService;

    public QueueController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/joinQueue")
    public Booking joinQueue(@RequestParam Long userId, @RequestParam Long centerId) {
        return bookingService.joinQueue(userId, centerId);
    }

    @GetMapping("/queueStatus/{userId}")
    public List<Booking> getQueueStatus(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }
}
