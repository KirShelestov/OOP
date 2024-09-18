package ru.nsu.shelestov;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения и обработки успеваемости студента.
 */
public class StudentRecordBook {
    List<List<Grade>> semesters; // 8 семестров
    private boolean isNotPaid; // false если платное, true если бюджет

    /**
     * Конструктор класса.
     *
     * @param isNotPaid платка/бюджет
     */
    public StudentRecordBook(boolean isNotPaid) {
        this.semesters = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            semesters.add(new ArrayList<>());
        }
        this.isNotPaid = isNotPaid;
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
        if (semester < 1 || semester > 8) {
            throw new IllegalArgumentException("Числа от 1 до 8");
        }
        if (controlType == ControlType.CREDIT) {
            if (!(score instanceof Boolean)) {
                throw new IllegalArgumentException("Для зачета необходимо передать boolean значение");
            }
            boolean scoreBool = (boolean) score;
            int scoreInt = scoreBool ? 5 : 2;
            semesters.get(semester - 1).add(new Grade(subject, controlType, scoreInt));
        } else {
            if (!(score instanceof Integer)) {
                throw new IllegalArgumentException("Для остальных необходимо"
                       + " передать int значение");
            }
            int scoreInt = (int) score;
            semesters.get(semester - 1).add(new Grade(subject, controlType, scoreInt));
        }
    }

    /**
     * Метод для подсчета среднего балла.
     *
     * @return средний балл
     */
    public double calculateGpa() {
        int totalScore = 0;
        int totalGrades = 0;

        for (List<Grade> semester : semesters) {
            for (Grade grade : semester) {
                if (grade.getControlType() != ControlType.CREDIT) {
                    totalScore += grade.getScore();
                    totalGrades++;
                }
            }
        }

        return totalGrades == 0 ? 0 : (double) totalScore / totalGrades;
    }

    /**
     * Метод на проверку перевода на бюджет.
     *
     * @param currentSemester текущий семестр
     * @return возможен ли перевод
     */
    public boolean canTransferToBudget(int currentSemester) {

        if (isNotPaid) {
            return  false;
        }
        if (currentSemester < 2) {
            return  false;
        }
        int failedExamsCount = 0;
        for (int i = (currentSemester - 2); i < currentSemester; i++) {
            List<Grade> semester = semesters.get(i);
            for (Grade grade : semester) {
                if (grade.isExam() && grade.getScore() < 3) {
                    failedExamsCount++;
                }
                if (grade.getControlType() == ControlType.DIFFERENTIAL_CREDIT) {
                    if (grade.isCredit() && grade.getScore() == 2) {
                        return false;
                    }
                }
            }
        }
        return !isNotPaid && failedExamsCount == 0;
    }

    /**
     * Метод для проверки возможности получения красного диплома.
     *
     * @return возможно ли получение диплома
     */
    public boolean canGetRedDiploma() {
        int excellentCount = 0;
        int totalCount = 0;
        boolean hasUnsatisfactory = false;
        boolean greatGrade = false;
        boolean isEightthSemester = false;
        for (List<Grade> semester : semesters) {
            for (Grade grade : semester) {
                if (grade.getScore() < 4) {
                    hasUnsatisfactory = true;
                }
                if (grade.getScore() >= 4) {
                    excellentCount++;
                }
                if (grade.getControlType() == ControlType.THESIS_DEFENSE) {
                    isEightthSemester = true;
                    if (grade.getScore() == 5) {
                        greatGrade = true;
                    }
                }
                totalCount++;
            }
        }

        if (isEightthSemester) {
            if (greatGrade) {
                return !hasUnsatisfactory && (excellentCount * 100.0 / totalCount >= 75);
            } else {
                return false;
            }
        } else {
            return !hasUnsatisfactory && (excellentCount * 100.0 / totalCount >= 75);
        }
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

        List<Grade> semesterGrades = semesters.get(currentSemester - 1);

        boolean hasExcellent = semesterGrades.stream().anyMatch(grade -> grade.getScore() >= 4);

        return hasExcellent && semesterGrades.stream().noneMatch(grade -> grade.getScore() <= 3);
    }


}

/**
 * Перечисление типо контрольных работ.
 */
enum ControlType {
    ASSIGNMENT,
    TEST,
    COLLOQUIUM,
    EXAM,
    DIFFERENTIAL_CREDIT,
    CREDIT,
    PRACTICE_REPORT_DEFENSE,
    THESIS_DEFENSE
}

/**
 * Класс для хранения информации об оценке за контрольную работу.
 */
class Grade {
    private String subject;
    private ControlType controlType;
    private int score;

    /**
     * Конструктор класса оценки.
     *
     * @param subject предмет
     * @param controlType тип работы
     * @param score оценка
     */
    public Grade(String subject, ControlType controlType, int score) {
        this.subject = subject;
        this.controlType = controlType;
        this.score = score;
    }

    /**
     * Геттер оценки.
     *
     * @return оценка
     */
    public int getScore() {
        return score;
    }

    /**
     * Геттер типа работы.
     *
     * @return тип работы
     */
    public  ControlType getControlType() {
        return  controlType;
    }

    /**
     * Метод на проверку типа работы - экзамен.
     *
     * @return является ли работа экзаменом
     */
    public boolean isExam() {
        return controlType == ControlType.EXAM;
    }

    /**
     * Метод на проверку типа работы - зачет/дифзачет.
     *
     * @return является ли работа каким-то зачетом
     */
    public boolean isCredit() {
        return controlType == ControlType.CREDIT
                || controlType == ControlType.DIFFERENTIAL_CREDIT;
    }
}
