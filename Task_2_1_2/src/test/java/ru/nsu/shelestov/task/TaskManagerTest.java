package ru.nsu.shelestov.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager manager;
    private static final String WORKER_ID = "worker1";
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        manager = new TaskManager(tempDir.resolve("progress.json").toString());
    }
    
    @Test
    void testDistributeArray() {
        int[] numbers = {1, 2, 3, 4, 5};
        manager.distributeArray(numbers);
        
        Task task = manager.getNextTask(WORKER_ID);
        assertNotNull(task);
        assertArrayEquals(numbers, task.getNumbers());
    }
    
    @Test
    void testProcessResult() {
        int[] numbers = {4, 5, 6}; 
        manager.distributeArray(numbers);
        
        Task task = manager.getNextTask(WORKER_ID);
        assertNotNull(task);
        
        TaskResult result = new TaskResult(
            task.getId(),
            true,
            Arrays.asList(4, 6),
            WORKER_ID,
            100
        );
        
        manager.processResult(result);
        assertTrue(manager.isComplete());
        
        assertNull(manager.getNextTask(WORKER_ID));
    }
    
    @Test
    void testProgressPersistence() {
        int[] numbers = {1, 2, 3};
        manager.distributeArray(numbers);

        Task originalTask = manager.getNextTask(WORKER_ID);
        assertNotNull(originalTask, "Original task should not be null");
        assertArrayEquals(numbers, originalTask.getNumbers(), "Original task should have correct numbers");
        
        manager.returnTask(originalTask);
        
        String progressFile = tempDir.resolve("progress.json").toString();
        System.out.println("Progress file location: " + progressFile);
        
        File file = new File(progressFile);
        assertTrue(file.exists(), "Progress file should exist");
        assertTrue(file.length() > 0, "Progress file should not be empty");
        
        TaskManager newManager = new TaskManager(progressFile);

        Task restoredTask = newManager.getNextTask(WORKER_ID);
        assertNotNull(restoredTask, "Restored task should not be null");
        assertArrayEquals(numbers, restoredTask.getNumbers(), "Restored task should have same numbers");
        assertEquals(originalTask.getId(), restoredTask.getId(), "Task IDs should match");
    }
}