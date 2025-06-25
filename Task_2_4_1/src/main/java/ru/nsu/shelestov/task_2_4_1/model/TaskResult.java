public class TaskResult {
    private TaskStatus status;
    private List<String> styleViolations;
    private TestResults testResults;
    private int points;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public List<String> getStyleViolations() {
        return styleViolations;
    }

    public void setStyleViolations(List<String> styleViolations) {
        this.styleViolations = styleViolations;
    }

    public TestResults getTestResults() {
        return testResults;
    }

    public void setTestResults(TestResults testResults) {
        this.testResults = testResults;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}