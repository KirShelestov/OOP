package ru.nsu.shelestov.recordbook;

/**
 * Класс для хранения информации об оценке за контрольную работу.
 */
public class Grade {
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
     * Геттер предмета.
     *
     * @return предмет
     */
    public String getSubject() {
        return subject;
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

