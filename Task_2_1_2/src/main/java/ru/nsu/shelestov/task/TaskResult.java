package ru.nsu.shelestov.task;

import java.io.Serializable;
import java.util.List;

public class TaskResult implements Serializable {
    private final String taskId;
    private final boolean hasComposite;
    private final List<Integer> compositeNumbers;
    private final String workerId;
    private final long processingTime;

    public TaskResult(String taskId, boolean hasComposite, List<Integer> compositeNumbers, 
                     String workerId, long processingTime) {
        this.taskId = taskId;
        this.hasComposite = hasComposite;
        this.compositeNumbers = compositeNumbers;
        this.workerId = workerId;
        this.processingTime = processingTime;
    }

    public String getTaskId() { return taskId; }
    public boolean hasComposite() { return hasComposite; }
    public List<Integer> getCompositeNumbers() { return compositeNumbers; }
    public String getWorkerId() { return workerId; }
    public long getProcessingTime() { return processingTime; }
}
