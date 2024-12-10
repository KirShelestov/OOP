package ru.nsu.shelestov.recordbook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentRecordBookTest {
    private StudentRecordBook recordBook;
    private StudentRecordBook notPaidrecordBook;
    private static final String TEST_FILENAME = "src/test/resources/test_recordbook.txt";

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
        notPaidrecordBook = new StudentRecordBook(false, semesterConfigs);
    }

    @Test
    void testAddGrade() {
        recordBook.addGrade(1, new Grade("Math", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Physics", ControlType.EXAM, 4));
        recordBook.addGrade(1, new Grade("Chemistry", ControlType.CREDIT, true));

        assertEquals(2, recordBook.semesters.get(0).get(ControlType.EXAM).size());
        assertEquals(1, recordBook.semesters.get(0).get(ControlType.CREDIT).size());
    }

    @Test
    void testAddGradeExceedLimit() {
        recordBook.addGrade(1, new Grade("Math", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Physics", ControlType.EXAM, 4));

        assertThrows(IllegalArgumentException.class, () -> {
            recordBook.addGrade(1, new Grade("Chemistry", ControlType.EXAM, 3)); // Превышение лимита
        });
    }

    @Test
    void testCalculateGpa() {
        recordBook.addGrade(1, new Grade("Math", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Physics", ControlType.EXAM, 4));
        recordBook.addGrade(1, new Grade("Chemistry", ControlType.CREDIT, true));

        double gpa = recordBook.calculateGpa();
        assertEquals(4.5, gpa, 0.01);
    }

    @Test
    void testCanGetRedDiploma() {
        recordBook.addGrade(1, new Grade("Math", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Physics", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Chemistry", ControlType.CREDIT, true));

        assertTrue(recordBook.canGetRedDiploma());
    }

    @Test
    void testCanGetIncreasedScholarship() {
        recordBook.addGrade(1, new Grade("Math", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Physics", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Chemistry", ControlType.CREDIT, true));
        recordBook.addGrade(1, new Grade("OBJ", ControlType.DIFFERENTIAL_CREDIT, 5));

        assertTrue(recordBook.canGetIncreasedScholarship(1));
    }

    @Test
    void testCantGetIncreasedScholarship() {
        notPaidrecordBook.addGrade(1, new Grade("Math", ControlType.EXAM, 2));
        notPaidrecordBook.addGrade(1, new Grade("Physics", ControlType.EXAM, 5));
        notPaidrecordBook.addGrade(1, new Grade("Chemistry", ControlType.CREDIT, true));

        assertFalse(notPaidrecordBook.canGetIncreasedScholarship(1));
    }

    @Test
    void testCanTransferToBudgetPaid() {
        notPaidrecordBook.addGrade(1, new Grade("Math", ControlType.EXAM, 5));
        notPaidrecordBook.addGrade(1, new Grade("Physics", ControlType.EXAM, 5));
        notPaidrecordBook.addGrade(1, new Grade("Chemistry", ControlType.CREDIT, true));
        notPaidrecordBook.addGrade(1, new Grade("OBJ", ControlType.DIFFERENTIAL_CREDIT, 5));
        notPaidrecordBook.addGrade(2, new Grade("OBJ", ControlType.DIFFERENTIAL_CREDIT, 5));

        notPaidrecordBook.addGrade(2, new Grade("Math", ControlType.EXAM, 4));
        notPaidrecordBook.addGrade(2, new Grade("Physics", ControlType.EXAM, 5));
        notPaidrecordBook.addGrade(2, new Grade("Chemistry", ControlType.CREDIT, true));

        assertFalse(notPaidrecordBook.canTransferToBudget(2));
    }

    @Test
    void testCanTransferToBudget() {
        recordBook.addGrade(1, new Grade("Math", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Physics", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Chemistry", ControlType.CREDIT, true));
        recordBook.addGrade(1, new Grade("OBJ", ControlType.DIFFERENTIAL_CREDIT, 5));
        recordBook.addGrade(2, new Grade("OBJ", ControlType.DIFFERENTIAL_CREDIT, 5));

        recordBook.addGrade(2, new Grade("Math", ControlType.EXAM, 4));
        recordBook.addGrade(2, new Grade("Physics", ControlType.EXAM, 5));
        recordBook.addGrade(2, new Grade("Chemistry", ControlType.CREDIT, true));

        assertTrue(recordBook.canTransferToBudget(2));
    }

    @Test
    void testCanAddSubjectWithDifferentControlTypesInDifferentSemesters() {
        recordBook.addGrade(1, new Grade("Mathematics", ControlType.EXAM, 4));

        assertDoesNotThrow(() -> {
            recordBook.addGrade(2, new Grade("Mathematics", ControlType.CREDIT, true));
        });
    }


    @Test
    void testSerializeToTXT() {
        recordBook.addGrade(1, new Grade("Mathematics", ControlType.EXAM, 5));
        recordBook.addGrade(1, new Grade("Physics", ControlType.CREDIT, true));

        recordBook.serializeToTXT(TEST_FILENAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILENAME))) {
            String line = reader.readLine();
            assertEquals("Semester,ControlType,Subject,Grade,IsCredit", line);

            line = reader.readLine();

            assertEquals("1,EXAM,\"Mathematics\",5,false", line);

            line = reader.readLine();
            assertEquals("1,CREDIT,\"Physics\",5,true", line);

        } catch (IOException e) {
            fail("IOException while reading the TXT file: " + e.getMessage());
        }
    }


}
