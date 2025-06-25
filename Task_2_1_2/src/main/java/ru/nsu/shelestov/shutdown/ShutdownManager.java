package ru.nsu.shelestov.shutdown;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ShutdownManager {
    private static final Logger logger = Logger.getLogger(ShutdownManager.class.getName());
    private final Set<String> activeWorkers;
    private final ExecutorService executorService;
    private volatile boolean shutdownInitiated = false;
    private final Object shutdownLock = new Object();

    public ShutdownManager(ExecutorService executorService) {
        this.activeWorkers = ConcurrentHashMap.newKeySet();
        this.executorService = executorService;
    }

    public void registerWorker(String workerId) {
        activeWorkers.add(workerId);
        logger.info("Worker registered: " + workerId);
    }

    public void unregisterWorker(String workerId) {
        activeWorkers.remove(workerId);
        logger.info("Worker unregistered: " + workerId);
    }

    public void initiateShutdown(String reason) {
        synchronized (shutdownLock) {
            if (shutdownInitiated) {
                return;
            }
            shutdownInitiated = true;
            logger.info("Initiating shutdown. Reason: " + reason);
        }
    }

    public boolean isShutdownInitiated() {
        return shutdownInitiated;
    }

    public void shutdown() {
        logger.info("Starting graceful shutdown...");
        
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.severe("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        logger.info("Shutdown completed. Active workers at shutdown: " + activeWorkers.size());
    }

    public Set<String> getActiveWorkers() {
        return activeWorkers;
    }
}