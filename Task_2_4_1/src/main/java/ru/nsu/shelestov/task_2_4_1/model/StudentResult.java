public class StudentResult {
    private final Student student;
    private final Map<String, TaskResult> taskResults = new HashMap<>();
    
    public StudentResult(Student student) {
        this.student = student;
    }
    
    public void addTaskResult(String taskId, TaskResult result) {
        taskResults.put(taskId, result);
    }
    
    public int getTotalPoints() {
        return taskResults.values().stream()
                .mapToInt(TaskResult::getPoints)
                .sum();
    }
}