package ru.nsu.shelestov.task;

import java.io.Serializable;
import java.util.UUID;
import java.util.Arrays;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String id;
    private final int[] numbers;
    private final int startIndex;
    private final int endIndex;
    private String assignedWorkerId;
    private long assignedTime;
    
    public Task(int[] numbers, int startIndex, int endIndex) {
        this.id = UUID.randomUUID().toString();
        this.numbers = numbers;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.assignedTime = -1;
        this.assignedWorkerId = null;
    }
    
    public void assignTo(String workerId) {
        this.assignedWorkerId = workerId;
        this.assignedTime = System.currentTimeMillis();
    }
    
    /**
     * Resets the task's assignment information, making it available for reassignment
     */
    public void reset() {
        this.assignedWorkerId = null;
        this.assignedTime = -1;
    }
    
    public String getId() {
        return id;
    }
    
    public int[] getNumbers() {
        return numbers;
    }
    
    public int getStartIndex() {
        return startIndex;
    }
    
    public int getEndIndex() {
        return endIndex;
    }
    
    public String getAssignedWorkerId() {
        return assignedWorkerId;
    }

    public long getAssignedTime() {
        return assignedTime;
    }
}
