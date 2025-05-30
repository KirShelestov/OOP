package ru.nsu.shelestov.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskQueueTest {
    private TaskQueue queue;
    private static final int TIMEOUT_MS = 100;
    private static final String WORKER_ID = "worker1";
    
    @BeforeEach
    void setUp() {
        queue = new TaskQueue(TIMEOUT_MS);
    }
    
    @Test
    void testAddAndGetTask() {
        Task task = new Task(new int[]{1, 2, 3}, 0, 3);
        queue.addTask(task);
        
        Task retrieved = queue.getNext(WORKER_ID);
        assertNotNull(retrieved);
        assertEquals(task.getId(), retrieved.getId());
        assertEquals(WORKER_ID, retrieved.getAssignedWorkerId());
        assertTrue(retrieved.getAssignedTime() > 0);
    }
    
    @Test
    void testTaskTimeout() throws InterruptedException {
        Task task = new Task(new int[]{1, 2, 3}, 0, 3);
        queue.addTask(task);
        
        Task retrieved = queue.getNext(WORKER_ID);
        assertNotNull(retrieved);
        assertEquals(1, queue.getActiveTasks().size());
        
        Thread.sleep(TIMEOUT_MS + 50);
        queue.checkTimeouts();
        
        assertEquals(0, queue.getActiveTasks().size());
        assertEquals(1, queue.getPendingTasks().size());
    }
    
    @Test
    void testCompleteTask() {
        Task task = new Task(new int[]{1, 2, 3}, 0, 3);
        queue.addTask(task);
        
        Task retrieved = queue.getNext(WORKER_ID);
        assertNotNull(retrieved);
        assertEquals(1, queue.getActiveTasks().size());
        
        queue.completeTask(retrieved.getId());
        assertEquals(0, queue.getActiveTasks().size());
        assertEquals(0, queue.getPendingTasks().size());
    }
    
    @Test
    void testIsComplete() {
        assertTrue(queue.isComplete());
        
        Task task = new Task(new int[]{1, 2, 3}, 0, 3);
        queue.addTask(task);
        assertFalse(queue.isComplete());
        
        Task retrieved = queue.getNext(WORKER_ID);
        assertFalse(queue.isComplete());
        
        queue.completeTask(retrieved.getId());
        assertTrue(queue.isComplete());
    }
}