package com.queueease.backend.dto;

import lombok.Data;

@Data
public class ServeUserRequest {
    private Long userId;
    private Long centerId;
}
