package ru.nsu.shelestov.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.time.Instant;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PerformanceMetrics {
    private static final Logger logger = Logger.getLogger(PerformanceMetrics.class.getName());
    private final Map<String, AtomicLong> networkTimeByWorker = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> computeTimeByWorker = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> taskCountByWorker = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public void recordNetworkTime(String workerId, long timeMs) {
        networkTimeByWorker.computeIfAbsent(workerId, k -> new AtomicLong(0))
                          .addAndGet(timeMs);
    }

    public void recordComputeTime(String workerId, long timeMs) {
        computeTimeByWorker.computeIfAbsent(workerId, k -> new AtomicLong(0))
                          .addAndGet(timeMs);
    }

    public void incrementTaskCount(String workerId) {
        taskCountByWorker.computeIfAbsent(workerId, k -> new AtomicLong(0))
                        .incrementAndGet();
    }

    public void saveMetrics(String filename) {
        MetricsData data = new MetricsData();
        data.timestamp = Instant.now().toString();
        data.networkTimeByWorker = convertToMap(networkTimeByWorker);
        data.computeTimeByWorker = convertToMap(computeTimeByWorker);
        data.taskCountByWorker = convertToMap(taskCountByWorker);

        try {
            Path metricsDir = Paths.get("metrics");
            Files.createDirectories(metricsDir);
            
            Path metricsFile = metricsDir.resolve(filename);
            try (FileWriter writer = new FileWriter(metricsFile.toFile())) {
                gson.toJson(data, writer);
                logger.info("Metrics saved to " + metricsFile.toAbsolutePath());
            }
        } catch (Exception e) {
            logger.severe("Failed to save metrics: " + e.getMessage());
        }
    }

    private Map<String, Long> convertToMap(Map<String, AtomicLong> atomicMap) {
        Map<String, Long> result = new ConcurrentHashMap<>();
        atomicMap.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    private static class MetricsData {
        String timestamp;
        Map<String, Long> networkTimeByWorker;
        Map<String, Long> computeTimeByWorker;
        Map<String, Long> taskCountByWorker;
    }
}