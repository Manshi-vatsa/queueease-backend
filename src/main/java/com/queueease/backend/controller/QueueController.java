package com.queueease.backend.controller;

import com.queueease.backend.repository.QueueRepository;
import com.queueease.backend.repository.BookingRepository; // ✅ ADDED
import com.queueease.backend.model.Queue;
import com.queueease.backend.model.Booking;
import com.queueease.backend.service.BookingService;
import com.queueease.backend.service.QueueService;
import com.queueease.backend.mongo.QueueActivityService;
import com.queueease.backend.dto.BookingResponse;
import com.queueease.backend.dto.QueueStatusResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;
    private final BookingService bookingService;
    private final QueueRepository queueRepository;
    private final QueueActivityService activityService;
    private final BookingRepository bookingRepository; // ✅ ADDED

    // ✅ UPDATED CONSTRUCTOR
    public QueueController(BookingService bookingService,
                           QueueRepository queueRepository,
                           QueueService queueService,
                           QueueActivityService activityService,
                           BookingRepository bookingRepository) { // ✅ ADDED
        this.bookingService = bookingService;
        this.queueRepository = queueRepository;
        this.queueService = queueService;
        this.activityService = activityService;
        this.bookingRepository = bookingRepository; // ✅ ADDED
    }

    // ✅ JOIN QUEUE (UNCHANGED)
    @PostMapping("/joinQueue")
    public BookingResponse joinQueue(@RequestParam Long userId,
                                     @RequestParam Long centerId) {
        return bookingService.joinQueue(userId, centerId);
    }

    // ✅ SERVE NEXT (UPDATED WITH DELETE LOGIC)
    @PostMapping("/serveNext/{centerId}")
    public Queue serveNext(@PathVariable Long centerId) {

        Queue queue = queueService.getOrCreateQueue(centerId);

        if (queue.getCurrentServingNumber() == null) {
            queue.setCurrentServingNumber(0);
        }

        if (queue.getCurrentServingNumber() >= queue.getCurrentNumber()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No more people in queue");
        }

        // ✅ INCREMENT
        queue.setCurrentServingNumber(queue.getCurrentServingNumber() + 1);

        // ✅ ADD THIS BLOCK (REMOVE SERVED USER)
        Booking booking = bookingRepository
                .findByCenterIdAndQueueNumber(centerId, queue.getCurrentServingNumber());

        if (booking != null) {
            bookingRepository.delete(booking);
        }

        Queue updatedQueue = queueRepository.save(queue);

        // ✅ LOG AFTER SUCCESS
        activityService.log(null, centerId, "SERVE_NEXT");

        return updatedQueue;
    }

    // ✅ USER BOOKINGS (UNCHANGED)
    @GetMapping("/queueStatus/{userId}")
    public List<Booking> getQueueStatus(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    // ✅ SMART STATUS (UNCHANGED)
    @GetMapping("/status")
    public QueueStatusResponse getQueueStatus(
            @RequestParam Long userId,
            @RequestParam Long centerId) {

        return bookingService.getQueueStatus(userId, centerId);
    }

    // ✅ CURRENT QUEUE (UNCHANGED)
    @GetMapping("/current/{centerId}")
    public Queue getCurrentQueue(@PathVariable Long centerId) {
        return queueRepository.findByCenterId(centerId)
                .orElseGet(() -> {
                    Queue newQueue = new Queue();
                    newQueue.setCenterId(centerId);
                    newQueue.setCurrentServingNumber(0);
                    return queueRepository.save(newQueue);
                });
    }
}