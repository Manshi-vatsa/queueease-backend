package com.queueease.backend.controller;

import com.queueease.backend.model.Queue;
import com.queueease.backend.model.Booking;
import com.queueease.backend.service.BookingService;
import com.queueease.backend.service.QueueService;
import com.queueease.backend.mongo.QueueActivityService;
import com.queueease.backend.dto.QueueStatusResponse;
import com.queueease.backend.dto.ServeUserRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;
    private final BookingService bookingService;
    private final QueueActivityService activityService;

    public QueueController(QueueService queueService,
                           BookingService bookingService,
                           QueueActivityService activityService) {
        this.queueService = queueService;
        this.bookingService = bookingService;
        this.activityService = activityService;
    }

    // ✅ JOIN QUEUE (FINAL CLEAN)
    @PostMapping("/joinQueue")
    public ResponseEntity<?> joinQueue(
            @RequestParam Long userId,
            @RequestParam Long centerId) {

        Booking booking = queueService.joinQueue(userId, centerId);

        // optional logging
        activityService.log(userId, centerId, "JOIN_QUEUE");

        return ResponseEntity.ok(booking);
    }

    // ✅ SERVE NEXT
    @PutMapping("/serveNext/{centerId}")
    public Queue serveNext(@PathVariable Long centerId) {

        Queue queue = queueService.getOrCreateQueue(centerId);

        if (queue.getCurrentServingNumber() == null) {
            queue.setCurrentServingNumber(0);
        }

        if (queue.getCurrentServingNumber() >= queue.getCurrentNumber()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No more people in queue"
            );
        }

        queue.setCurrentServingNumber(queue.getCurrentServingNumber() + 1);

        Queue updated = queueService.updateQueue(queue);

        activityService.log(null, centerId, "SERVE_NEXT");

        return updated;
    }

    // ✅ USER BOOKINGS
    @GetMapping("/queueStatus/{userId}")
    public List<Booking> getQueueStatus(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    // ✅ SMART STATUS
    @GetMapping("/status")
    public QueueStatusResponse getQueueStatus(
            @RequestParam Long userId,
            @RequestParam Long centerId) {

        return bookingService.getQueueStatus(userId, centerId);
    }

    // QUEUE LIST
    @GetMapping("/list")
    public List<Booking> getQueueList(@RequestParam Long centerId) {
        return bookingService.getQueueUsers(centerId);
    }

    // QUEUE USERS - Get queue users for a specific center
    @GetMapping("/users")
    public List<Booking> getQueueUsers(@RequestParam Long centerId) {
        return bookingService.getQueueUsers(centerId);
    }

    // CURRENT QUEUE
    @GetMapping("/current/{centerId}")
    public Queue getCurrentQueue(@PathVariable Long centerId) {
        return queueService.getOrCreateQueue(centerId);
    }

    // SERVE USER - Admin-driven serving with proper validation and atomic updates
    @PostMapping("/serveUser")
    public ResponseEntity<QueueStatusResponse> serveUser(@RequestBody ServeUserRequest request) {
        try {
            QueueStatusResponse response = bookingService.serveUser(request.getUserId(), request.getCenterId());
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serve user: " + e.getMessage());
        }
    }
}