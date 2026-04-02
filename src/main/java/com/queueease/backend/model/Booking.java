package com.queueease.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "user_id")     // ✅ MUST match DB
    private Long userId;

    @Column(name = "center_id")   // ✅ MUST match DB
    private Long centerId;

    @Column(name = "queue_number")
    private Integer queueNumber;
}