package com.queueease.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class QueueStatusResponse {
    private String recommendation;
    private int queueNumber;
    private int currentServing;     
    private int peopleAhead;
    private int estimatedWaitTime;
    
    // New fields for serveUser response
    private Integer currentServingNumber;
    private List<?> remainingUsers;
    private String message;
    
    // Existing getters and setters
    public int getQueueNumber() {
        return queueNumber;
    }
    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }
    public int getCurrentServing() {
        return currentServing;
    }
    public void setCurrentServing(int currentServing) {
        this.currentServing = currentServing;
    }
    public int getPeopleAhead() {
        return peopleAhead;
    }
    public void setPeopleAhead(int peopleAhead) {
        this.peopleAhead = peopleAhead;
    }
    public int getEstimatedWaitTime() {
        return estimatedWaitTime;
    }
    public void setEstimatedWaitTime(int estimatedWaitTime) {
        this.estimatedWaitTime = estimatedWaitTime;
    }
    public String getRecommendation() {
        return recommendation;
    }
    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
    
    // New getters and setters for serveUser response
    public Integer getCurrentServingNumber() {
        return currentServingNumber;
    }
    public void setCurrentServingNumber(Integer currentServingNumber) {
        this.currentServingNumber = currentServingNumber;
    }
    public List<?> getRemainingUsers() {
        return remainingUsers;
    }
    public void setRemainingUsers(List<?> remainingUsers) {
        this.remainingUsers = remainingUsers;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}