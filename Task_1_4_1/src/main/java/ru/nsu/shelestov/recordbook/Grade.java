package ru.nsu.shelestov.recordbook;

/**
 * Класс для хранения информации об оценке за контрольную работу.
 */
public class Grade {
    private String subject;
    private ControlType controlType;
    private Object score; // Изменяем тип score на Object

    /**
     * Конструктор класса оценки.
     *
     * @param subject предмет
     * @param controlType тип работы
     * @param score оценка (int)
     */
    public Grade(String subject, ControlType controlType, int score) {
        this.subject = subject;
        this.controlType = controlType;
        this.score = score;
    }

    /**
     * Новый конструктор класса оценки, который принимает boolean для зачетов.
     *
     * @param subject     предмет
     * @param controlType тип работы
     * @param isCredit    является ли работа зачетом
     */
    public Grade(String subject, ControlType controlType, boolean isCredit) {
        this.subject = subject;
        this.controlType = controlType; // Use the passed control type
        this.score = isCredit; // Здесь score будет хранить boolean
    }

    /**
     * Геттер оценки.
     *
     * @return оценка в виде int, если это экзамен, или boolean, если это зачет.
     */
    public Object getScore() {
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
    public ControlType getControlType() {
        return controlType;
    }

    /**
     * Метод для получения оценки в виде int.
     *
     * @return оценка в виде int
     */
    public int getScoreAsInt() {
        if (controlType == ControlType.CREDIT) {
            if (score instanceof Boolean) {
                return (Boolean) score ? 5 : 2; // Convert boolean to int
            } else {
                throw new IllegalArgumentException("Score must be a Boolean for credits");
            }
        } else if (score instanceof Integer) {
            return (Integer) score; // Return the integer score
        } else {
            throw new IllegalArgumentException("Score must be an Integer or Boolean");
        }
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
        return controlType == ControlType.CREDIT || controlType == ControlType.DIFFERENTIAL_CREDIT;
    }
}
