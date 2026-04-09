package com.queueease.backend.controller;

import com.queueease.backend.repository.QueueRepository;
import com.queueease.backend.model.Queue;
import com.queueease.backend.model.Booking;
import com.queueease.backend.service.BookingService;
import com.queueease.backend.service.QueueService;
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

    public QueueController(BookingService bookingService,
                       QueueRepository queueRepository,
                       QueueService queueService) {
    this.bookingService = bookingService;
    this.queueRepository = queueRepository;
    this.queueService = queueService;
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

    Queue queue = queueService.getOrCreateQueue(centerId);

    if (queue.getCurrentServingNumber() == null) {
        queue.setCurrentServingNumber(0);
    }

    if (queue.getCurrentServingNumber() >= queue.getCurrentNumber()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No more people in queue");
    }

    queue.setCurrentServingNumber(queue.getCurrentServingNumber() + 1);

    return queueRepository.save(queue);
}

    // ✅ USER BOOKINGS
    @GetMapping("/queueStatus/{userId}")
    public List<Booking> getQueueStatus(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }
    @GetMapping("/status")
public QueueStatusResponse getQueueStatus(
        @RequestParam Long userId,
        @RequestParam Long centerId) {

    return bookingService.getQueueStatus(userId, centerId);
}

    // ✅ CURRENT QUEUE
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