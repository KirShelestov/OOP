package ru.nsu.shelestov.monitoring;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import ru.nsu.shelestov.task.Task;
import ru.nsu.shelestov.task.TaskManager;

public class HealthCheck implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(HealthCheck.class.getName());
    private final Map<String, WorkerStatus> workerStatuses;
    private final ScheduledExecutorService scheduler;
    private final long healthCheckIntervalSeconds;
    private final long workerTimeoutSeconds;
    private final TaskManager taskManager; 

    public HealthCheck(TaskManager taskManager, long healthCheckIntervalSeconds, 
            long workerTimeoutSeconds) {
        this.taskManager = taskManager;
        this.workerStatuses = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.healthCheckIntervalSeconds = healthCheckIntervalSeconds;
        this.workerTimeoutSeconds = workerTimeoutSeconds;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(
            this::checkWorkersHealth,
            healthCheckIntervalSeconds,
            healthCheckIntervalSeconds,
            TimeUnit.SECONDS
        );
        logger.info("HealthCheck started with interval " + healthCheckIntervalSeconds + " seconds");
    }

    public void updateWorkerStatus(String workerId) {
        workerStatuses.put(workerId, new WorkerStatus(System.currentTimeMillis()));
        logger.fine("Updated status for worker: " + workerId);
    }

    private void checkWorkersHealth() {
        long currentTime = System.currentTimeMillis();
        workerStatuses.forEach((workerId, status) -> {
            long timeSinceLastUpdate = (currentTime - status.getLastUpdateTime()) / 1000;
            if (timeSinceLastUpdate > workerTimeoutSeconds) {
                logger.warning("Worker " + workerId + " appears to be dead. " +
                             "Last seen " + timeSinceLastUpdate + " seconds ago");
                handleDeadWorker(workerId);  
                workerStatuses.remove(workerId);
            }
        });
    }

    private void handleDeadWorker(String workerId) {
        try {
            List<Task> deadWorkerTasks = taskManager.getWorkerTasks(workerId);
            
            if (!deadWorkerTasks.isEmpty()) {
                logger.info("Found " + deadWorkerTasks.size() + 
                    " tasks from dead worker " + workerId);
                
                for (Task task : deadWorkerTasks) {
                    logger.info("Rescheduling task " + task.getId() + 
                        " from dead worker " + workerId);
                    taskManager.rescheduleTask(task);
                }
            }
        } catch (Exception e) {
            logger.severe("Error handling dead worker " + workerId + ": " + e.getMessage());
        }
    }

    public boolean isWorkerAlive(String workerId) {
        WorkerStatus status = workerStatuses.get(workerId);
        if (status == null) {
            return false;
        }
        
        long timeSinceLastUpdate = 
            (System.currentTimeMillis() - status.getLastUpdateTime()) / 1000;
        return timeSinceLastUpdate <= workerTimeoutSeconds;
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

    private static class WorkerStatus {
        private final long lastUpdateTime;

        public WorkerStatus(long lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public long getLastUpdateTime() {
            return lastUpdateTime;
        }
    }
}