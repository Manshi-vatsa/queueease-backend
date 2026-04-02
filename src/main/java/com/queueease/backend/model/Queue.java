package com.queueease.backend.model;

import jakarta.persistence.*;

@Entity
public class Queue {

    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "queue_id")   // 🔥 VERY IMPORTANT
private Long id;
    private Long centerId;

    // ✅ ADD THIS (NEW FIELD)
    private Integer currentNumber = 0;

    private Integer currentServingNumber = 0;

    // ✅ GETTERS + SETTERS

    public Long getId() {
        return id;
    }

    public Long getCenterId() {
        return centerId;
    }

    public void setCenterId(Long centerId) {
        this.centerId = centerId;
    }

    // 🔥 ADD THESE (your question)
    public Integer getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(Integer currentNumber) {
        this.currentNumber = currentNumber;
    }

    public Integer getCurrentServingNumber() {
        return currentServingNumber;
    }

    public void setCurrentServingNumber(Integer currentServingNumber) {
        this.currentServingNumber = currentServingNumber;
    }
}