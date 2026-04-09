package com.queueease.backend.service;

import com.queueease.backend.model.Queue;
import com.queueease.backend.repository.QueueRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QueueService {

    private final QueueRepository queueRepository;

    public QueueService(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    // ✅ Get or create queue
    public Queue getQueueByCenterId(Long centerId) {
        return queueRepository.findByCenterId(centerId).orElseGet(() -> {
            Queue q = new Queue();
            q.setCenterId(centerId);
            q.setCurrentNumber(0);              // ✅ ADD THIS
            q.setCurrentServingNumber(0);
            return queueRepository.save(q);
        });
    }
    public Queue getOrCreateQueue(Long centerId) {
    return queueRepository.findByCenterId(centerId)
        .orElseGet(() -> {
            Queue newQueue = new Queue();
            newQueue.setCenterId(centerId);
            newQueue.setCurrentServingNumber(0);
            newQueue.setCurrentNumber(0);
            return queueRepository.save(newQueue);
        });
}

    // ✅ Increment when user joins
  public Queue incrementQueue(Long centerId) {

   Queue queue = queueRepository.findByCenterId(centerId)
        .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Queue not found for centerId: " + centerId
        ));

    // ✅ Correct field to increment
    if (queue.getCurrentNumber() == null) {
        queue.setCurrentNumber(1);
    } else {
        queue.setCurrentNumber(queue.getCurrentNumber() + 1);
    }

    return queueRepository.save(queue);
}
}