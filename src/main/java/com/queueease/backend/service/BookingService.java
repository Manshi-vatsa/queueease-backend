package com.queueease.backend.service;

import com.queueease.backend.model.Booking;
import com.queueease.backend.model.Queue;
import com.queueease.backend.mongo.QueueActivityService;
import com.queueease.backend.repository.BookingRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.queueease.backend.dto.QueueStatusResponse;
import com.queueease.backend.dto.BookingResponse;
import java.util.List;

@Service
public class BookingService {
     private final QueueActivityService activityService;
    private final BookingRepository bookingRepository;
    private final QueueService queueService;
    

   public BookingService(BookingRepository bookingRepository,
                      QueueService queueService,
                      QueueActivityService activityService) {
    this.bookingRepository = bookingRepository;
    this.queueService = queueService;
    this.activityService = activityService;
}

    // ✅ EXISTING (UNCHANGED)
    public BookingResponse joinQueue(Long userId, Long centerId) {
        System.out.println("JOIN QUEUE CALLED");
      
        if (userId == null || centerId == null) {
            throw new RuntimeException("Invalid input");
        }
        List<Booking> existingBookings =
        bookingRepository.findByUserIdAndCenterId(userId, centerId);

// ✅ NEW LOGIC (ALLOW REJOIN IF ALREADY SERVED)
boolean activeBookingExists = existingBookings.stream()
    .anyMatch(b -> b.getQueueNumber() >= queueService
        .getQueueByCenterId(centerId)
        .getCurrentServingNumber());

if (activeBookingExists) {
    throw new ResponseStatusException(HttpStatus.CONFLICT, "User already in queue");
}

        Queue queue = queueService.incrementQueue(centerId);

        if (queue.getCurrentNumber() == null) {
            queue.setCurrentNumber(1);
        }

        int queueNumber = queue.getCurrentNumber();

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCenterId(centerId);
        booking.setQueueNumber(queueNumber);

        Booking savedBooking = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();
        response.setUserId(userId);
        response.setQueueNumber(savedBooking.getQueueNumber());
        response.setMessage("Successfully joined queue");

         activityService.log(userId, centerId, "JOIN_QUEUE");

        return response;
    }

    // ✅ EXISTING (UNCHANGED)
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // ✅ NEW METHOD (ADDED SAFELY)
    public QueueStatusResponse getQueueStatus(Long userId, Long centerId) {

        // 🔹 Get latest booking for this user & center
        List<Booking> bookings =
                bookingRepository.findByUserIdAndCenterId(userId, centerId);

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }

        // 👉 Take latest booking
        Booking booking = bookings.get(bookings.size() - 1);

        // 🔹 Get queue (already handled by your QueueService)
        Queue queue = queueService.getQueueByCenterId(centerId);

        if (queue == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue not found");
        }

        int queueNumber = booking.getQueueNumber();
        int currentServing = queue.getCurrentServingNumber();

        int peopleAhead = queueNumber - currentServing;
        if (peopleAhead < 0) peopleAhead = 0;

       int avgServiceTime = (int) activityService.getAverageServiceTime(centerId);
        int waitTime = peopleAhead * avgServiceTime;

        // 🔹 Build response
        QueueStatusResponse response = new QueueStatusResponse();
        response.setQueueNumber(queueNumber);
        response.setCurrentServing(currentServing);
        response.setPeopleAhead(peopleAhead);
        response.setEstimatedWaitTime(waitTime);

        String recommendation;

if (waitTime > 30) {
    recommendation = "Come later";
} else if (waitTime < 10) {
    recommendation = "Come now";
} else {
    recommendation = "You can plan accordingly";
}

response.setRecommendation(recommendation);

        return response;
    }
}