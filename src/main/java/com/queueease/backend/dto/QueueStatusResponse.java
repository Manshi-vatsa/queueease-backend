package com.queueease.backend.dto;

public class QueueStatusResponse {
    private String recommendation;
    private int queueNumber;
    private int currentServing;     
    private int peopleAhead;
    private int estimatedWaitTime;
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
    
}