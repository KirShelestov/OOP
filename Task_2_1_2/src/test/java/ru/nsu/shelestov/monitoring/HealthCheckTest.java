import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import ru.nsu.shelestov.task.Task;
import ru.nsu.shelestov.task.TaskManager;
import ru.nsu.shelestov.monitoring.HealthCheck;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HealthCheckTest {
    private HealthCheck healthCheck;

    @AfterEach
    void tearDown() throws Exception {
        if (healthCheck != null) {
            healthCheck.close();
        }
    }

    @Test
    void shouldDetectDeadWorkerAndRescheduleTask() throws Exception {
        TaskManager taskManager = mock(TaskManager.class);
        List<Task> deadWorkerTasks = Arrays.asList(
            new Task(new int[]{1, 2, 3}, 0, 1)
        );
        when(taskManager.getWorkerTasks("worker1")).thenReturn(deadWorkerTasks);
        
        healthCheck = new HealthCheck(taskManager, 1, 2);
        healthCheck.start();
        
        healthCheck.updateWorkerStatus("worker1");
        
        TimeUnit.SECONDS.sleep(3);
        
        verify(taskManager, timeout(4000)).getWorkerTasks("worker1");
        
        verify(taskManager, timeout(4000)).rescheduleTask(any(Task.class));
        
        assertFalse(healthCheck.isWorkerAlive("worker1"));
    }
}