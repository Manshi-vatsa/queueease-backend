package com.queueease.backend.service;

import com.queueease.backend.model.Queue;
import com.queueease.backend.repository.QueueRepository;
import org.springframework.stereotype.Service;

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

    // ✅ Increment when user joins
  public Queue incrementQueue(Long centerId) {

    Queue queue = queueRepository.findByCenterId(centerId)
            .orElseThrow(() -> new RuntimeException("Queue not found"));

    // ✅ Correct field to increment
    if (queue.getCurrentNumber() == null) {
        queue.setCurrentNumber(1);
    } else {
        queue.setCurrentNumber(queue.getCurrentNumber() + 1);
    }

    return queueRepository.save(queue);
}
}