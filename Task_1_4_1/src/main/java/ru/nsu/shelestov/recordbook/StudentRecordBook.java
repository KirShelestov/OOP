package ru.nsu.shelestov.recordbook;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для хранения и обработки успеваемости студента.
 */
public class StudentRecordBook {
    private List<Map<ControlType, Integer>> maxGradesPerSemester;
    List<Map<ControlType, List<Grade>>> semesters; // 8 семестров
    private boolean isNotPaid; // false если платное, true если бюджет

    /**
     * Конструктор класса.
     *
     * @param isNotPaid платка/бюджет
     * @param semesterConfigs список конфигураций семестро
     */
    public StudentRecordBook(boolean isNotPaid, List<Map<ControlType, Integer>> semesterConfigs) {
        this.semesters = new ArrayList<>();
        this.maxGradesPerSemester = new ArrayList<>();
        this.isNotPaid = isNotPaid;

        for (Map<ControlType, Integer> controlTypeMap : semesterConfigs) {
            Map<ControlType, List<Grade>> semester = new HashMap<>();
            for (Map.Entry<ControlType, Integer> entry : controlTypeMap.entrySet()) {
                semester.put(entry.getKey(), new ArrayList<>());
            }
            this.semesters.add(semester);
            this.maxGradesPerSemester.add(controlTypeMap);
        }
    }

    /**
     * Метод для добавления оценки.
     *
     * @param semester семестр
     * @param grade оценка
     */
    public void addGrade(int semester, Grade grade) {
        if (semester < 1 || semester > semesters.size()) {
            throw new IllegalArgumentException("Числа от 1 до " + semesters.size());
        }

        Map<ControlType, List<Grade>> controlTypeMap = semesters.get(semester - 1);
        List<Grade> grades = controlTypeMap.get(grade.getControlType());

        boolean subjectExists = grades.stream()
                .anyMatch(existingGrade -> existingGrade.getSubject().equals(grade.getSubject()));

        if (subjectExists) {
            throw new IllegalArgumentException("Предмет " + grade.getSubject() + " уже добавлен");
        }

        int maxGrades = maxGradesPerSemester.get(semester - 1).get(grade.getControlType());

        if (grades.size() >= maxGrades) {
            throw new IllegalArgumentException("Достигнуто максимальное количество оценок");
        }

        grades.add(grade); // Add the grade directly
    }

    /**
     * Метод для подсчета среднего балла.
     *
     * @return средний балл
     */
    public double calculateGpa() {
        return semesters.stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .filter(grade -> grade.getControlType() != ControlType.CREDIT)
                .mapToDouble(grade -> {
                    if (grade.isCredit()) {
                        return grade.getScoreAsInt(); // Use the method to get score as int
                    } else {
                        return grade.getScoreAsInt(); // This should return int for exams
                    }
                })
                .average()
                .orElse(0);
    }

    /**
     * Метод на проверку перевода на бюджет.
     *
     * @param currentSemester текущий семестр
     * @return возможен ли перевод
     */
    public boolean canTransferToBudget(int currentSemester) {
        if (!isNotPaid || currentSemester < 2) {
            return false;
        }

        long failedExamsCount = semesters.subList(currentSemester - 2, currentSemester).stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .filter(grade -> grade.isExam() && grade.getScoreAsInt() < 3)
                .count();

        boolean hasUnsatisfactoryCredits
                = semesters.subList(currentSemester - 2, currentSemester).stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .anyMatch(grade -> grade.getControlType() == ControlType.DIFFERENTIAL_CREDIT
                        && grade.isCredit() && grade.getScoreAsInt() == 2);

        return failedExamsCount == 0 && !hasUnsatisfactoryCredits;
    }

    /**
     * Метод для проверки возможности получения красного диплома.
     *
     * @return возможно ли получение диплома
     */
    public boolean canGetRedDiploma() {
        long totalCount = semesters.stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .count();

        long excellentCount = semesters.stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .filter(grade -> grade.getScoreAsInt() >= 4)
                .count();

        boolean hasUnsatisfactory = semesters.stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .anyMatch(grade -> grade.getScoreAsInt() < 4);

        boolean greatGrade = semesters.stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .filter(grade -> grade.getControlType() == ControlType.THESIS_DEFENSE)
                .anyMatch(grade -> grade.getScoreAsInt() == 5);

        boolean isEighthSemester = semesters.stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .anyMatch(grade -> grade.getControlType() == ControlType.THESIS_DEFENSE);

        return (isEighthSemester && greatGrade && !hasUnsatisfactory
                && (excellentCount * 100.0 / totalCount >= 75))
                || (!isEighthSemester && !hasUnsatisfactory && (excellentCount * 100.0
                / totalCount >= 75));
    }

    /**
     * Метод для проверки возможности получения повышенной стипендии.
     *
     * @param currentSemester текущий семестр
     * @return возможно ли получение стипендии.
     */
    public boolean canGetIncreasedScholarship(int currentSemester) {
        if (!isNotPaid) {
            return false;
        }

        Map<ControlType, List<Grade>> controlTypeMap = semesters.get(currentSemester - 1);

        List<Grade> semesterGrades = controlTypeMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        boolean hasExcellent = semesterGrades.stream().anyMatch(grade -> grade.getScoreAsInt() >= 4);
        boolean hasUnsatisfactory
                = semesterGrades.stream().anyMatch(grade -> grade.getScoreAsInt() <= 3);

        return hasExcellent && (!hasUnsatisfactory);
    }


    /**
     * Записываем зачетку в файл.
     * @param filename путь до файла
     */
    public void serializeToTXT(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Semester,ControlType,Subject,Grade,IsCredit");
            writer.newLine();

            for (int semester = 0; semester < semesters.size(); semester++) {
                Map<ControlType, List<Grade>> controlTypeMap = semesters.get(semester);
                for (ControlType controlType : controlTypeMap.keySet()) {
                    for (Grade grade : controlTypeMap.get(controlType)) {
                        writer.write(String.format("%d,%s,%s,%d,%s",
                                semester + 1,
                                controlType.name(),
                                escape(grade.getSubject()),
                                grade.getScoreAsInt(),
                                grade.isCredit()));
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Избавляемся от кавычек.
     *
     * @param value строка с ковычками
     * @return исправленная строка
     */
    private static String escape(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }


}
