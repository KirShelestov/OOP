package ru.nsu.shelestov.task;

import ru.nsu.shelestov.storage.ProgressStorage;
import ru.nsu.shelestov.storage.FileProgressStorage;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.io.IOException;

public class TaskManager {
    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());
    private static final int DEFAULT_CHUNK_SIZE = 1000;
    private static final int DEFAULT_TIMEOUT_MS = 30000;
    
    final TaskQueue taskQueue;
    private final AtomicBoolean compositeFound;
    private final ProgressStorage storage;
    
    public TaskManager(String progressFile) {
        this.storage = new FileProgressStorage(progressFile);
        this.taskQueue = loadOrCreateTaskQueue();
        this.compositeFound = new AtomicBoolean(false);
    }

    private TaskQueue loadOrCreateTaskQueue() {
        try {
            TaskQueue loaded = storage.loadProgress();
            if (loaded != null) {
                logger.info("Restored queue state: " + 
                           "pending tasks=" + loaded.getPendingTasks().size() + 
                           ", active tasks=" + loaded.getActiveTasks().size());
                return loaded;
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Failed to load progress: " + e.getMessage());
            e.printStackTrace();
        }
        logger.info("Creating new TaskQueue");
        return new TaskQueue(DEFAULT_TIMEOUT_MS);
    }

    public void clearProgress() {
        try {
            storage.clear();
            logger.info("Progress cleared successfully");
        } catch (IOException e) {
            logger.warning("Failed to clear progress: " + e.getMessage());
        }
    }

    public synchronized void processResult(TaskResult result) {
        taskQueue.completeTask(result.getTaskId());
        
        if (result.hasComposite()) {
            logger.info("Composite numbers found by worker " + result.getWorkerId() + 
                       ": " + result.getCompositeNumbers());
            compositeFound.set(true);
        }
        
        saveProgress();
    }

    public void checkTimeouts() {
        taskQueue.checkTimeouts();
        saveProgress();
    }

    public boolean isComplete() {
        return taskQueue.isComplete() || compositeFound.get();
    }

    public TaskStatistics getStatistics() {
        return new TaskStatistics(
            taskQueue.getPendingTasks().size(),
            taskQueue.getActiveTasks().size(),
            compositeFound.get()
        );
    }

    public void cancelAllTasks() {
        taskQueue.getPendingTasks().clear();
        taskQueue.getActiveTasks().clear();
        saveProgress();
    }


    public void distributeArray(int[] numbers) {
        Task task = new Task(numbers, 0, numbers.length);
        taskQueue.addTask(task);
        saveProgress(); 
    }

    public Task getNextTask(String workerId) {
        if (compositeFound.get()) {
            return null;
        }
        Task task = taskQueue.getNext(workerId);
        saveProgress();
        return task;
    }

    public void returnTask(Task task) {
        logger.info("Returning task: " + task.getId());
        taskQueue.addTask(task);
        saveProgress();
    }

    public synchronized void addTask(Task task) {
        taskQueue.addTask(task);
        saveProgress();
    }

    public List<Task> getWorkerTasks(String workerId) {
        return taskQueue.getTasksByWorkerId(workerId);
    }

    public void rescheduleTask(Task task) {
        taskQueue.returnTaskToQueue(task);
        try {
            storage.saveProgress(taskQueue);
        } catch (IOException e) {
            logger.warning("Failed to save progress after rescheduling task: " + e.getMessage());
        }
    }

    private void saveProgress() {
        try {
            logger.info("Saving progress. Queue state: " + 
                       "pending tasks=" + taskQueue.getPendingTasks().size() + 
                       ", active tasks=" + taskQueue.getActiveTasks().size());
            storage.saveProgress(taskQueue);
        } catch (IOException e) {
            logger.severe("Failed to save progress: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

