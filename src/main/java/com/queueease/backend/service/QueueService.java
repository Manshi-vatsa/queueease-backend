package com.queueease.backend.service;



import com.queueease.backend.model.Queue;
import com.queueease.backend.repository.QueueRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QueueService {

    private final QueueRepository queueRepository;

    public QueueService(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    public Queue getQueueByCenterId(Long centerId) {
        return queueRepository.findByCenterId(centerId).orElseGet(() -> {
            Queue q = new Queue();
            q.setCenterId(centerId);
            q.setCurrentServingNumber(0);
            return queueRepository.save(q);
        });
    }

    public Queue incrementQueue(Long centerId) {
        Queue queue = getQueueByCenterId(centerId);
        queue.setCurrentServingNumber(queue.getCurrentServingNumber() + 1);
        return queueRepository.save(queue);
    }
}
