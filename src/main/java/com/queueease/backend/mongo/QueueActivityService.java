package com.queueease.backend.mongo;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QueueActivityService {

    private final QueueActivityRepository repository;

    public QueueActivityService(QueueActivityRepository repository) {
        this.repository = repository;
    }

    public void log(Long userId, Long centerId, String action) {

        QueueActivity activity = new QueueActivity();
        activity.setUserId(userId);
        activity.setCenterId(centerId);
        activity.setAction(action);
        activity.setTimestamp(LocalDateTime.now());

        repository.save(activity);
     }
     public double getAverageServiceTime(Long centerId) {
    List<QueueActivity> logs = repository.findByCenterId(centerId);

    long serveCount = logs.stream()
            .filter(log -> "SERVE_NEXT".equals(log.getAction()))
            .count();

    if (serveCount == 0) return 5; // fallback

    // assume total time approx = serveCount * 5 (basic version)
    return 5; // keep simple for now
}


}