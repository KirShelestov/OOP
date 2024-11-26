package ru.nsu.shelestov.recordbook;

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
     * @param subject предмет
     * @param controlType тип контрольной работы
     * @param score оценка
     */
    public void addGrade(int semester, String subject, ControlType controlType, Object score) {
        if (semester < 1 || semester > semesters.size()) {
            throw new IllegalArgumentException("Числа от 1 до " + semesters.size());
        }
        Map<ControlType, List<Grade>> controlTypeMap = semesters.get(semester - 1);
        List<Grade> grades = controlTypeMap.get(controlType);

        boolean subjectExists = controlTypeMap.values().stream()
                .flatMap(List::stream)
                .anyMatch(grade -> grade.getSubject().equals(subject));

        if (subjectExists) {
            throw new IllegalArgumentException("Предмет " + subject + " уже добавлен");
        }

        int maxGrades = maxGradesPerSemester.get(semester - 1).get(controlType);

        if (grades.size() >= maxGrades) {
            throw new IllegalArgumentException("Достигнуто максимальное количество оцено]");
        }

        if (controlType == ControlType.CREDIT) {
            if (!(score instanceof Boolean)) {
                throw new IllegalArgumentException("Для зачета необходимо передать значение");
            }
            boolean scoreBool = (boolean) score;
            int scoreInt = scoreBool ? 5 : 2;
            grades.add(new Grade(subject, controlType, scoreInt));
        } else {
            if (!(score instanceof Integer)) {
                throw new IllegalArgumentException("Для остальных необходимо передать");
            }
            int scoreInt = (int) score;
            grades.add(new Grade(subject, controlType, scoreInt));
        }
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
                .mapToInt(Grade::getScore)
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
        if (isNotPaid || currentSemester < 2) {
            return false;
        }

        long failedExamsCount = semesters.subList(currentSemester - 2, currentSemester).stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .filter(grade -> grade.isExam() && grade.getScore() < 3)
                .count();

        boolean hasUnsatisfactoryCredits
                = semesters.subList(currentSemester - 2, currentSemester).stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .anyMatch(grade -> grade.getControlType() == ControlType.DIFFERENTIAL_CREDIT
                        && grade.isCredit() && grade.getScore() == 2);

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
                .filter(grade -> grade.getScore() >= 4)
                .count();

        boolean hasUnsatisfactory = semesters.stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .anyMatch(grade -> grade.getScore() < 4);

        boolean greatGrade = semesters.stream()
                .flatMap(controlTypeMap -> controlTypeMap.values().stream())
                .flatMap(List::stream)
                .filter(grade -> grade.getControlType() == ControlType.THESIS_DEFENSE)
                .anyMatch(grade -> grade.getScore() == 5);

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

        Map<ControlType, List<Grade>> controlTypeMap = semesters.get(currentSemester);

        List<Grade> semesterGrades = controlTypeMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        boolean hasExcellent = semesterGrades.stream().anyMatch(grade -> grade.getScore() >= 4);
        boolean hasUnsatisfactory
                = semesterGrades.stream().anyMatch(grade -> grade.getScore() <= 3);

        return hasExcellent && (!hasUnsatisfactory);
    }
}
