package ru.nsu.shelestov.worker;

import java.io.Serializable;
import java.util.List;

public class ProcessResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String taskId;
    private final List<Integer> compositeNumbers;

    public ProcessResult(String taskId, List<Integer> compositeNumbers) {
        this.taskId = taskId;
        this.compositeNumbers = compositeNumbers;
    }

    public String getTaskId() {
        return taskId;
    }

    public List<Integer> getCompositeNumbers() {
        return compositeNumbers;
    }
}