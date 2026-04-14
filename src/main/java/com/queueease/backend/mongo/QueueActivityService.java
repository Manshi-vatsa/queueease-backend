package com.queueease.backend.mongo;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueActivityService {

    // ✅ 1. DECLARE FIRST
    private final MongoTemplate mongoTemplate;

    // ✅ 2. CONSTRUCTOR
    public QueueActivityService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // ✅ 3. LOG METHOD
    public void log(Long userId, Long centerId, String action) {

        Document log = new Document();

        log.append("userId", userId);
        log.append("centerId", centerId);
        log.append("action", action);
        log.append("timestamp", java.time.LocalDateTime.now());
     int hour = (int)(Math.random() * 24);
log.append("hour", hour);

        // Optional (for analytics)
       int estimatedWaitTime = (int)(Math.random() * 20) + 1;
log.append("estimatedWaitTime", estimatedWaitTime);

        mongoTemplate.insert(log, "queue_logs");
    }

    // ✅ 4. AVERAGE WAIT TIME
    public double getAverageWaitTime() {

        List<Document> result = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.group().avg("estimatedWaitTime").as("avgWait")
                ),
                "queue_logs",
                Document.class
        ).getMappedResults();

        if (result.isEmpty() || result.get(0).get("avgWait") == null) {
            return 0.0;
        }

        return result.get(0).getDouble("avgWait");
    }

    // ✅ 5. PEAK HOURS
    public List<Document> getPeakHours() {

        return mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.group("hour").count().as("count")
                ),
                "queue_logs",
                Document.class
        ).getMappedResults();
    }
}