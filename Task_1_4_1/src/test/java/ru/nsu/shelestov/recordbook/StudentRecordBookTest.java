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
    private StudentRecordBook recordBookPaid;
    private StudentRecordBook recordBookNotPaid;

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
        // Initialize both paid and not paid record books
        recordBookPaid = new StudentRecordBook(false, semesterConfigs); // isNotPaid = false
        recordBookNotPaid = new StudentRecordBook(true, semesterConfigs); // isNotPaid = true
    }

    // Test for isNotPaid = false
    @Test
    void testAddGradePaid() {
        recordBookPaid.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBookPaid.addGrade(1, "Physics", ControlType.EXAM, 4);
        recordBookPaid.addGrade(1, "Chemistry", ControlType.CREDIT, true);

        assertEquals(2, recordBookPaid.semesters.get(0).get(ControlType.EXAM).size());
        assertEquals(1, recordBookPaid.semesters.get(0).get(ControlType.CREDIT).size());
    }

    @Test
    void testCanGetIncreasedScholarshipPaid() {
        recordBookPaid.addGrade(1, "Math", ControlType.EXAM, 2);
        recordBookPaid.addGrade(1, "Physics", ControlType.EXAM, 5);
        recordBookPaid.addGrade(1, "Chemistry", ControlType.CREDIT, true);

        assertFalse(recordBookPaid.canGetIncreasedScholarship(1)); // Should return false for paid students
    }

    @Test
    void testCanTransferToBudgetPaid() {
        recordBookPaid.addGrade(2, "Math", ControlType.EXAM, 4);
        recordBookPaid.addGrade(2, "Physics", ControlType.EXAM, 5);
        recordBookPaid.addGrade(2, "Chemistry", ControlType.CREDIT, true);

        assertTrue(recordBookPaid.canTransferToBudget(2)); // Should return false for paid students
    }

    // Test for isNotPaid = true
    @Test
    void testAddGradeNotPaid() {
        recordBookNotPaid.addGrade(1, "Math", ControlType.EXAM, 5);
        recordBookNotPaid.addGrade(1, "Physics", ControlType.EXAM, 4);
        recordBookNotPaid.addGrade(1, "Chemistry", ControlType.CREDIT, true);

        assertEquals(2, recordBookNotPaid.semesters.get(0).get(ControlType.EXAM).size());
        assertEquals(1, recordBookNotPaid.semesters.get(0).get(ControlType.CREDIT).size());
    }

    @Test
    void testCanGetIncreasedScholarshipNotPaid() {
        recordBookNotPaid.addGrade(1, "Math", ControlType.EXAM, 2);
        recordBookNotPaid.addGrade(1, "Physics", ControlType.EXAM, 5);
        recordBookNotPaid.addGrade(1, "Chemistry", ControlType.CREDIT, true);

        assertFalse(recordBookNotPaid.canGetIncreasedScholarship(1)); // Should return true for not paid students
    }

    @Test
    void testCanTransferToBudgetNotPaid() {
        recordBookNotPaid.addGrade(2, "Math", ControlType.EXAM, 4);
        recordBookNotPaid.addGrade(2, "Physics", ControlType.EXAM, 5);
        recordBookNotPaid.addGrade(2, "Chemistry", ControlType.CREDIT, true);

        assertTrue(recordBookNotPaid.canTransferToBudget(2)); // Should return true for not paid students
    }



    @Test
    void testCanAddSubjectWithDifferentControlTypesInDifferentSemesters() {
        recordBookNotPaid.addGrade(1, "Mathematics", ControlType.EXAM, 4);

        assertDoesNotThrow(() -> {
            recordBookNotPaid.addGrade(2, "Mathematics", ControlType.CREDIT, true);
        });
    }
}
