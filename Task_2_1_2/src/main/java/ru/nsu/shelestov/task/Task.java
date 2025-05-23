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
    private long assignedTime;
    private String workerId;

    public Task(int[] numbers, int startIndex, int endIndex) {
        this.id = UUID.randomUUID().toString();
        this.numbers = Arrays.copyOf(numbers, numbers.length);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.assignedTime = 0;
        this.workerId = null;
    }

    public String getId() { return id; }
    public int[] getNumbers() { return Arrays.copyOf(numbers, numbers.length); }
    public int getStartIndex() { return startIndex; }
    public int getEndIndex() { return endIndex; }
    public long getAssignedTime() { return assignedTime; }
    public String getWorkerId() { return workerId; }

    public void assignTo(String workerId) {
        this.workerId = workerId;
        this.assignedTime = System.currentTimeMillis();
    }
}
