package com.queueease.backend.service;

import com.queueease.backend.model.Booking;
import com.queueease.backend.model.Queue;
import com.queueease.backend.repository.BookingRepository;
import com.queueease.backend.repository.QueueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueService {

    private final QueueRepository queueRepository;
    private final BookingRepository bookingRepository;

    public QueueService(QueueRepository queueRepository,
                        BookingRepository bookingRepository) {
        this.queueRepository = queueRepository;
        this.bookingRepository = bookingRepository;
    }
     public Queue updateQueue(Queue queue) {
    return queueRepository.save(queue);
}
    // ✅ Get or create queue
    public Queue getOrCreateQueue(Long centerId) {
        return queueRepository.findByCenterId(centerId)
                .orElseGet(() -> {
                    Queue queue = new Queue();
                    queue.setCenterId(centerId);
                    queue.setCurrentNumber(0);
                    queue.setCurrentServingNumber(0);
                    return queueRepository.save(queue);
                });
    }

    // ✅ JOIN QUEUE (FINAL)
    public Booking joinQueue(Long userId, Long centerId) {

        // 🔴 Remove old booking (avoid 409)
        List<Booking> existing =
                bookingRepository.findByUserIdAndCenterId(userId, centerId);

        if (!existing.isEmpty()) {
            bookingRepository.deleteAll(existing);
        }

        Queue queue = getOrCreateQueue(centerId);

        if (queue.getCurrentNumber() == null) {
            queue.setCurrentNumber(0);
        }

        int nextNumber = queue.getCurrentNumber() + 1;
        queue.setCurrentNumber(nextNumber);

        queueRepository.save(queue);

        // 🔥 SAVE BOOKING
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCenterId(centerId);
        booking.setQueueNumber(nextNumber);

        System.out.println("Saving booking: " + booking);

        Booking saved = bookingRepository.save(booking);

        System.out.println("Saved successfully");

        return saved;
    }

    public Queue incrementQueue(Long centerId) {

    Queue queue = getOrCreateQueue(centerId);

    if (queue.getCurrentNumber() == null) {
        queue.setCurrentNumber(1);
    } else {
        queue.setCurrentNumber(queue.getCurrentNumber() + 1);
    }

    return queueRepository.save(queue);
}

    // ✅ GET QUEUE LIST
    public List<Booking> getQueueList(Long centerId) {
        return bookingRepository.findByCenterIdOrderByQueueNumberAsc(centerId);
    }
}