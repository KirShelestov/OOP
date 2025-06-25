package ru.nsu.shelestov.storage;

import ru.nsu.shelestov.task.TaskQueue;
import java.io.IOException;

public interface ProgressStorage {
    void saveProgress(TaskQueue queue) throws IOException;
    TaskQueue loadProgress() throws IOException, ClassNotFoundException;
    void clear() throws IOException;
}