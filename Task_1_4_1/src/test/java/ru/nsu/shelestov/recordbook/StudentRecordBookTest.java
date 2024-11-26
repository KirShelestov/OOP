package ru.nsu.shelestov.recordbook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentRecordBookTest {
    private StudentRecordBook recordBook;

    @BeforeEach
    void setUp() {
        List<Map<ControlType, Integer>> semesterConfigs = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Map<ControlType, Integer> controlTypes = new HashMap<>();
            controlTypes.put(ControlType.EXAM, 2);
            controlTypes.put(ControlType.CREDIT, 1);
            controlTypes.put(ControlType.DIFFERENTIAL_CREDIT, 1);
            semesterConfigs.add(controlTypes);
        }
        recordBook = new StudentRecordBook(true, semesterConfigs);
    }

    @Test
    void testAddGrade() {
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Physics", ControlType.EXAM, 4);
        recordBook.addGrade(1, "Chemistry", ControlType.CREDIT, true);

        assertEquals(2, recordBook.semesters.get(0).get(ControlType.EXAM).size());
        assertEquals(1, recordBook.semesters.get(0).get(ControlType.CREDIT).size());
    }

    @Test
    void testAddGradeExceedLimit() {
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Physics", ControlType.EXAM, 4);

        assertThrows(IllegalArgumentException.class, () -> {
            recordBook.addGrade(1, "Chemistry", ControlType.EXAM, 3); // Превышение лимита
        });
    }

    @Test
    void testCalculateGpa() {
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Physics", ControlType.EXAM, 4);
        recordBook.addGrade(1, "Chemistry", ControlType.CREDIT, true);

        double gpa = recordBook.calculateGpa();
        assertEquals(4.5, gpa, 0.01);
    }

    @Test
    void testCanGetRedDiploma() {
        recordBook.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Physics", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Chemistry", ControlType.CREDIT, true);

        assertTrue(recordBook.canGetRedDiploma());
    }

    @Test
    void testCanGetIncreasedScholarship() {
        recordBook.addGrade(1, "Math", ControlType.EXAM, 2);
        recordBook.addGrade(1, "Physics", ControlType.EXAM, 5);
        recordBook.addGrade(1, "Chemistry", ControlType.CREDIT, true);

        assertFalse(recordBook.canGetIncreasedScholarship(1));
    }

    @Test
    void testCanTransferToBudget() {
        recordBook.addGrade(2, "Math", ControlType.EXAM, 4);
        recordBook.addGrade(2, "Physics", ControlType.EXAM, 5);
        recordBook.addGrade(2, "Chemistry", ControlType.CREDIT, true);

        assertTrue(recordBook.canTransferToBudget(2));
    }

    @Test
    void testCannotAddSubjectWithDifferentControlTypesInSameSemester() {
        recordBook.addGrade(1, "Mathematics", ControlType.EXAM, 4);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            recordBook.addGrade(1, "Mathematics", ControlType.CREDIT, true); // Adding as CREDIT
        });

        String expectedMessage = "Предмет Mathematics уже добавлен в текущем семестре с другим видом контроля.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCanAddSubjectWithDifferentControlTypesInDifferentSemesters() {
        recordBook.addGrade(1, "Mathematics", ControlType.EXAM, 4);

        assertDoesNotThrow(() -> {
            recordBook.addGrade(2, "Mathematics", ControlType.CREDIT, true);
        });
    }
}
