package ru.nsu.shelestov.task_2_4_1.config;

import ru.nsu.shelestov.task_2_4_1.model.Checkpoint;
import ru.nsu.shelestov.task_2_4_1.model.Student;
import ru.nsu.shelestov.task_2_4_1.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    private List<Task> tasks;
    private Map<String, List<Student>> groups;
    private List<Checkpoint> checkpoints;
    private Map<String, List<String>> taskAssignments;
    private Map<String, Object> settings;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Map<String, List<Student>> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, List<Student>> groups) {
        this.groups = groups;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Map<String, List<String>> getTaskAssignments() {
        return taskAssignments;
    }

    public void setTaskAssignments(Map<String, List<String>> taskAssignments) {
        this.taskAssignments = taskAssignments;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }
}