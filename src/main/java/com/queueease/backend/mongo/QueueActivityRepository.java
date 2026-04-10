package com.queueease.backend.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface QueueActivityRepository extends MongoRepository<QueueActivity, String> {

    List<QueueActivity> findByCenterId(Long centerId);

}