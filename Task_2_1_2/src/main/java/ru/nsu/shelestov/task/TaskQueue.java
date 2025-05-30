package ru.nsu.shelestov.task;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.stream.Collectors;

public class TaskQueue implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final BlockingQueue<Task> pendingTasks;
    private final Map<String, Task> activeTasks;
    private final int timeoutMs;
    private volatile boolean complete;

    public TaskQueue(int timeoutMs) {
        this.timeoutMs = timeoutMs;
        this.pendingTasks = new LinkedBlockingQueue<>();
        this.activeTasks = new ConcurrentHashMap<>();
        this.complete = true; 
    }

    public boolean isComplete() {
        return complete && pendingTasks.isEmpty() && activeTasks.isEmpty();
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public BlockingQueue<Task> getPendingTasks() {
        return pendingTasks;
    }

    public Map<String, Task> getActiveTasks() {
        return activeTasks;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public void addTask(Task task) {
        pendingTasks.offer(task);
        setComplete(false); 
    }

    public Task getNext(String workerId) {
        Task task = pendingTasks.poll();
        if (task != null) {
            task.assignTo(workerId);
            activeTasks.put(task.getId(), task);
        }
        return task;
    }

    public void completeTask(String taskId) {
        activeTasks.remove(taskId);
        if (pendingTasks.isEmpty() && activeTasks.isEmpty()) {
            setComplete(true);
        }
    }

    public void checkTimeouts() {
        long now = System.currentTimeMillis();
        activeTasks.values().removeIf(task -> {
            if (now - task.getAssignedTime() > timeoutMs) {
                pendingTasks.offer(task);
                return true;
            }
            return false;
        });
    }

    public List<Task> getTasksByWorkerId(String workerId) {
        return activeTasks.values().stream()
            .filter(task -> workerId.equals(task.getAssignedWorkerId()))
            .collect(Collectors.toList());
    }

    public void returnTaskToQueue(Task task) {
        if (activeTasks.remove(task.getId()) != null) {
            task.reset();  // Сбрасываем информацию о назначении
            pendingTasks.offer(task);
        }
    }
}