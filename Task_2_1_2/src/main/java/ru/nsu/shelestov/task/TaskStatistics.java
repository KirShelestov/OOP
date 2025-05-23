package ru.nsu.shelestov.task;

public record TaskStatistics(int pendingTasks, int activeTasks, boolean compositeFound) {
}