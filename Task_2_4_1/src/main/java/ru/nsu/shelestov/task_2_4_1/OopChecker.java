package ru.nsu.shelestov.task_2_4_1;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import ru.nsu.shelestov.task_2_4_1.config.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OopChecker {
    private final Configuration config;
    private Map<String, StudentResult> results;

    private static final String WORK_DIR = "repositories";
    private static final String CHECKSTYLE_CONFIG = "google_checks.xml";

    public OopChecker() {
        this.config = loadConfiguration();
    }

    private Configuration loadConfiguration() {
        try {
            File configFile = new File("src/main/resources/config.groovy");
            String script = Files.readString(configFile.toPath());

            Binding binding = new Binding();
            binding.setVariable("config", new Configuration());

            GroovyShell shell = new GroovyShell(binding);
            shell.evaluate(script);

            return (Configuration) binding.getVariable("config");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public void check() {
        cloneRepositories();

        processStudentTasks();

        generateReport();
    }

    private void cloneRepositories() {
        File workDir = new File(WORK_DIR);
        if (!workDir.exists()) {
            workDir.mkdir();
        }

        verifyGitConfig();

        for (List<Student> students : config.getGroups().values()) {
            for (Student student : students) {
                String repoPath = WORK_DIR + "/" + student.getGithubUsername();
                File repoDir = new File(repoPath);

                try {
                    if (!repoDir.exists()) {
                        executeCommand("git clone " + student.getRepositoryUrl() + " " + repoPath);
                    } else {
                        executeCommand("cd " + repoPath + " && git pull");
                    }
                } catch (IOException e) {
                    System.err.println("Failed to clone/update repository for " + student.getGithubUsername());
                }
            }
        }
    }

    private void processStudentTasks() {
        Map<String, StudentResult> results = new HashMap<>();

        for (List<Student> students : config.getGroups().values()) {
            for (Student student : students) {
                StudentResult result = new StudentResult(student);
                String repoPath = WORK_DIR + "/" + student.getGithubUsername();

                List<String> assignedTasks = config.getTaskAssignments().get(student.getGithubUsername());
                if (assignedTasks == null) continue;

                for (String taskId : assignedTasks) {
                    Task task = config.getTaskById(taskId);
                    if (task == null) continue;

                    TaskResult taskResult = processTask(repoPath, task);
                    result.addTaskResult(taskId, taskResult);
                }

                results.put(student.getGithubUsername(), result);
            }
        }

        this.results = results;
    }

    private TaskResult processTask(String repoPath, Task task) {
        TaskResult result = new TaskResult();

        if (!compileProject(repoPath)) {
            result.setStatus(TaskStatus.COMPILATION_FAILED);
            return result;
        }

        if (!generateJavadoc(repoPath)) {
            result.setStatus(TaskStatus.DOCUMENTATION_FAILED);
            return result;
        }

        List<String> styleViolations = checkCodeStyle(repoPath);
        result.setStyleViolations(styleViolations);
        if (!styleViolations.isEmpty()) {
            result.setStatus(TaskStatus.STYLE_CHECK_FAILED);
            return result;
        }

        TestResults testResults = runTests(repoPath);
        result.setTestResults(testResults);

        int points = calculatePoints(task, result);
        result.setPoints(points);

        return result;
    }

    private void generateReport() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>OOP Course Results</title></head><body>");
        html.append("<h1>OOP Course Results</h1>");

        for (Map.Entry<String, List<Student>> group : config.getGroups().entrySet()) {
            html.append("<h2>Group ").append(group.getKey()).append("</h2>");
            html.append("<table border='1'><tr><th>Student</th><th>Tasks</th><th>Points</th><th>Grade</th></tr>");

            for (Student student : group.getValue()) {
                StudentResult result = results.get(student.getGithubUsername());
                if (result == null) continue;

                html.append("<tr>");
                html.append("<td>").append(student.getFullName()).append("</td>");
                html.append("<td>").append(generateTasksHtml(result)).append("</td>");
                html.append("<td>").append(result.getTotalPoints()).append("</td>");
                html.append("<td>").append(calculateGrade(result.getTotalPoints())).append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
        }

        html.append("</body></html>");

        try {
            Files.writeString(Path.of("report.html"), html.toString());
        } catch (IOException e) {
            System.err.println("Failed to generate report: " + e.getMessage());
        }
    }

    private void verifyGitConfig() {
        try {
            String output = executeCommand("git config --get user.name");
            if (output.trim().isEmpty()) {
                throw new RuntimeException("Git user.name is not configured");
            }
            output = executeCommand("git config --get user.email");
            if (output.trim().isEmpty()) {
                throw new RuntimeException("Git user.email is not configured");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to verify git configuration", e);
        }
    }

    private String executeCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        }
    }

    public static void main(String[] args) {
        try {
            OopChecker checker = new OopChecker();
            checker.check();
            System.out.println("Check completed. Report generated in report.html");
        } catch (Exception e) {
            System.err.println("Error during check: " + e.getMessage());
            e.printStackTrace();
        }
    }
}