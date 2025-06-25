package ru.nsu.shelestov.monitoring;

import ru.nsu.shelestov.task.TaskManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TaskTimeoutWatcher implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(TaskTimeoutWatcher.class.getName());
    private final TaskManager taskManager;
    private final ScheduledExecutorService scheduler;
    private final long checkIntervalSeconds;
    
    public TaskTimeoutWatcher(TaskManager taskManager, long checkIntervalSeconds) {
        this.taskManager = taskManager;
        this.checkIntervalSeconds = checkIntervalSeconds;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(
            this::checkTimeouts,
            checkIntervalSeconds,
            checkIntervalSeconds,
            TimeUnit.SECONDS
        );
        logger.info("TaskTimeoutWatcher started with interval " + checkIntervalSeconds + " seconds");
    }

    private void checkTimeouts() {
        try {
            taskManager.checkTimeouts();
        } catch (Exception e) {
            logger.severe("Error checking timeouts: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}