package ru.nsu.shelestov.storage;

import ru.nsu.shelestov.task.TaskQueue;
import java.io.*;

public class FileProgressStorage implements ProgressStorage {
    private final String filename;

    public FileProgressStorage(String filename) {
        this.filename = filename;
        createStorageFileIfNeeded();
    }

    private void createStorageFileIfNeeded() {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create storage file", e);
            }
        }
    }

    @Override
    public void saveProgress(TaskQueue queue) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(queue);
        }
    }

    @Override
    public TaskQueue loadProgress() throws IOException, ClassNotFoundException {
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (TaskQueue) in.readObject();
        }
    }

    @Override
    public void clear() throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete progress file");
            }
        }
        createStorageFileIfNeeded();
    }
}