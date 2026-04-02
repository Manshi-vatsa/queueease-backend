package com.queueease.backend.dto;

import lombok.Data;

@Data
public class BookingResponse {
    private Long userId;
    private Integer queueNumber;
    private String message;
}

