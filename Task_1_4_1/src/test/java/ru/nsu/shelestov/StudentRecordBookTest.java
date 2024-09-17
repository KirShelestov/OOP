package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Класс для тестирования.
 */
public class StudentRecordBookTest {

    /**
     * Тест на подсчет среднего балла.
     */
    @Test
    public void testCalculateGpa() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Physics", ControlType.TEST, 4);
        assertEquals(4.5, recordBook.calculateGPA(), 0.01);
    }

    /**
     * Тест на возможность перевода на бюджет.
     */
    @Test
    public void testCanTransferToBudget() {
        StudentRecordBook recordBook = new StudentRecordBook(false);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(2, "Physics", ControlType.EXAM, 4);
        assertTrue(recordBook.canTransferToBudget(3));
    }

    /**
     * Тест на возможность перевода на бюджет из-за того, что уже на бюджете.
     */
    @Test
    public void testCannotTransferToBudget() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(2, "Physics", ControlType.EXAM, 4);
        assertFalse(recordBook.canTransferToBudget(3));
    }

    /**
     * Тест на возможность полученмя красного дипдома.
     */
    @Test
    public void testCanGetRedDiploma() {
        StudentRecordBook recordBook = new StudentRecordBook(false);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(2, "Physics", ControlType.EXAM, 5);
        recordBook.addGrade(8, "VKR", ControlType.THESIS_DEFENSE, 5);
        assertTrue(recordBook.canGetRedDiploma());
    }

    /**
     * Тест на невозможность полученмя красного дипдома из-за "3".
     */
    @Test
    public void testCannotGetRedDiploma() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(2, "Physics", ControlType.EXAM, 3);
        recordBook.addGrade(8, "VKR", ControlType.THESIS_DEFENSE, 5);
        assertFalse(recordBook.canGetRedDiploma());
    }

    /**
     * Тест на невозможность полученмя красного дипдома из-за ВКР.
     */
    @Test
    public void testCannotGetRedDiplomaAgain() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(2, "Physics", ControlType.EXAM, 5);
        recordBook.addGrade(8, "VKR", ControlType.THESIS_DEFENSE, 4);
        assertFalse(recordBook.canGetRedDiploma());
    }

    /**
     * Тест на возможность получения стипендии.
     */
    @Test
    public void testCanGetIncreasedScholarship() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Physics", ControlType.TEST, 5);
        assertTrue(recordBook.canGetIncreasedScholarship(1));
    }

    /**
     * Тест на невозможность получения стипендии из-за "3".
     */
    @Test
    public void testCannotGetIncreasedScholarship() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        recordBook.addGrade(1, "Math", ControlType.TEST, 3);
        recordBook.addGrade(1, "Physics", ControlType.TEST, 4);
        assertFalse(recordBook.canGetIncreasedScholarship(1));
    }

    /**
     * Тест на невозможность получения стипендии из-за того, что на платке.
     */
    @Test
    public void testCannotGetIncreasedScholarshipAgain() {
        StudentRecordBook recordBook = new StudentRecordBook(false);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Physics", ControlType.TEST, 5);
        assertFalse(recordBook.canGetIncreasedScholarship(1));
    }

    /**
     * Тест на добавление оценки.
     */
    @Test
    public void testAddGrade() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        assertEquals(1, recordBook.semesters.get(0).size());
    }

    /**
     * Тест на добавление оценки в несуществуеющий семестр.
     */
    @Test
    public void testAddGradeInvalidSemester() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        assertThrows(IllegalArgumentException.class, () -> recordBook.addGrade(9, "Math", ControlType.EXAM, 5));
    }

    /**
     * Тест на добавление оценки, только с неправильным типом.
     */
    @Test
    public void testAddGradeInvalidScore() {
        StudentRecordBook recordBook = new StudentRecordBook(true);
        assertThrows(IllegalArgumentException.class, () -> recordBook.addGrade(1, "Math", ControlType.EXAM, "abc"));
    }
}