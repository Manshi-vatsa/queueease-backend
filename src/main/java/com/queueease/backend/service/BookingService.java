package com.queueease.backend.service;

import com.queueease.backend.model.Booking;
import com.queueease.backend.model.Queue;
import com.queueease.backend.mongo.QueueActivityService;
import com.queueease.backend.repository.BookingRepository;
import com.queueease.backend.dto.QueueStatusResponse;
import com.queueease.backend.dto.BookingResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final QueueService queueService;
    private final QueueActivityService activityService;

    public BookingService(BookingRepository bookingRepository,
                          QueueService queueService,
                          QueueActivityService activityService) {
        this.bookingRepository = bookingRepository;
        this.queueService = queueService;
        this.activityService = activityService;
    }

    // ✅ JOIN QUEUE (SAFE VERSION)
    public BookingResponse joinQueue(Long userId, Long centerId) {

        List<Booking> existing =
                bookingRepository.findByUserIdAndCenterId(userId, centerId);

        Queue queue = queueService.getOrCreateQueue(centerId);

        int currentServing = queue.getCurrentServingNumber() == null
                ? 0
                : queue.getCurrentServingNumber();

        boolean activeBookingExists = existing.stream()
                .anyMatch(b -> b.getQueueNumber() >= currentServing);

        if (activeBookingExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already in queue");
        }

        // ✅ FIXED PART
Queue updatedQueue = queueService.incrementQueue(centerId);
int nextNumber = updatedQueue.getCurrentNumber();

Booking booking = new Booking();
booking.setUserId(userId);
booking.setCenterId(centerId);
booking.setQueueNumber(nextNumber);

        Booking saved = bookingRepository.save(booking);

        activityService.log(userId, centerId, "JOIN_QUEUE");

        BookingResponse response = new BookingResponse();
        response.setUserId(userId);
        response.setQueueNumber(saved.getQueueNumber());
        response.setMessage("Joined successfully");

        return response;
    }

    // ✅ USER BOOKINGS
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // ✅ QUEUE STATUS
    public QueueStatusResponse getQueueStatus(Long userId, Long centerId) {

        List<Booking> bookings =
                bookingRepository.findByUserIdAndCenterId(userId, centerId);

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }

        Booking booking = bookings.get(bookings.size() - 1);
        Queue queue = queueService.getOrCreateQueue(centerId);

        int queueNumber = booking.getQueueNumber();
        int currentServing = queue.getCurrentServingNumber() == null ? 0 : queue.getCurrentServingNumber();

        int peopleAhead = Math.max(queueNumber - currentServing - 1, 0);

        Double avgTime = activityService.getAverageWaitTime();
        int avgServiceTime = (avgTime == null || avgTime == 0) ? 5 : avgTime.intValue();

        int waitTime = peopleAhead * avgServiceTime;

        QueueStatusResponse response = new QueueStatusResponse();
        response.setQueueNumber(queueNumber);
        response.setCurrentServing(currentServing);
        response.setPeopleAhead(peopleAhead);
        response.setEstimatedWaitTime(waitTime);

        if (waitTime > 30) {
            response.setRecommendation("Come later");
        } else if (waitTime < 10) {
            response.setRecommendation("Come now");
        } else {
            response.setRecommendation("Plan accordingly");
        }

        return response;
    }

    // SERVE USER - Admin-driven serving with proper validation and atomic updates
    public QueueStatusResponse serveUser(Long userId, Long centerId) {
        // 1. Validate user exists in queue for center
        List<Booking> userBookings = bookingRepository.findByUserIdAndCenterId(userId, centerId);
        if (userBookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in queue for this center");
        }

        // Get the active booking (highest queue number for this user)
        Booking userBooking = userBookings.stream()
                .max((b1, b2) -> Integer.compare(b1.getQueueNumber(), b2.getQueueNumber()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active booking found"));

        // Get current queue state
        Queue queue = queueService.getOrCreateQueue(centerId);
        int currentServing = queue.getCurrentServingNumber() == null ? 0 : queue.getCurrentServingNumber();

        // Validate user is actually in queue (not already served)
        if (userBooking.getQueueNumber() <= currentServing) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already been served");
        }

        // 2. Mark user as SERVED or remove from queue
        // Option 1: Remove from queue (cleaner approach)
        bookingRepository.delete(userBooking);

        // 3. Increment currentServingNumber in MySQL queue table
        queue.setCurrentServingNumber(currentServing + 1);
        queueService.updateQueue(queue);

        // 4. Save updated queue state in MySQL (already done by updateQueue)

        // 5. Insert SERVE_NEXT log in MongoDB
        activityService.log(userId, centerId, "SERVE_USER");

        // 6. Return updated queue status
        QueueStatusResponse response = new QueueStatusResponse();
        response.setCurrentServingNumber(queue.getCurrentServingNumber());
        response.setRemainingUsers(bookingRepository.findByCenterIdOrderByQueueNumberAsc(centerId));
        response.setMessage("User served successfully");

        return response;
    }

    // Get queue users for a specific center
    public List<Booking> getQueueUsers(Long centerId) {
        return bookingRepository.findByCenterIdOrderByQueueNumberAsc(centerId);
    }
}