import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordBookTest {

    static RecordBook badStudentRecordBook = new RecordBook();
    static RecordBook goodStudentRecordBook = new RecordBook();
    static RecordBook excellentStudentRecordBook = new RecordBook();

    @BeforeAll
    static void setBadStudentRB() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Math", Mark.THREE));
        semester.addSubject(new Subject("Haskell", Mark.FOUR));
        badStudentRecordBook.addSemester(semester);
    }

    @BeforeAll
    static void setGoodStudentRB() {
        Semester firstSemester = new Semester();
        firstSemester.addSubject(new Subject("Math", Mark.THREE));
        firstSemester.addSubject(new Subject("Haskell", Mark.FOUR));
        goodStudentRecordBook.addSemester(firstSemester);


        Semester secondSemester = new Semester();
        secondSemester.addSubject(new Subject("Math", Mark.FOUR));
        goodStudentRecordBook.addSemester(secondSemester);
        secondSemester = goodStudentRecordBook.getLastSemester();
        secondSemester.addSubject(new Subject("OOP", Mark.FIVE));
        secondSemester.addSubject(new Subject("Programming", Mark.FIVE));

        goodStudentRecordBook.setDiplomaWork(
                new Subject(
                        "Differences between Imperative and Declarative programming",
                        Mark.FIVE
                ));
    }

    @BeforeAll
    static void setExcellentStudentRB() {
        Semester firstSemester = new Semester();
        firstSemester.addSubject(new Subject("Math", Mark.FOUR));
        firstSemester.addSubject(new Subject("Haskell", Mark.FOUR));
        excellentStudentRecordBook.addSemester(firstSemester);

        Semester secondSemester = new Semester();
        secondSemester.addSubject(new Subject("Math", Mark.FIVE));
        secondSemester.addSubject(new Subject("OOP", Mark.FIVE));
        secondSemester.addSubject(new Subject("Project", Mark.FIVE));
        secondSemester.addSubject(new Subject("Operating Systems", Mark.FIVE));
        excellentStudentRecordBook.addSemester(secondSemester);

        excellentStudentRecordBook.setDiplomaWork(
                new Subject(
                        "Differences between Imperative and Declarative programming",
                        Mark.FIVE
                ));
    }

    @Test
    void stipendPossibilityTest1() {
        assertTrue(excellentStudentRecordBook.isStipendPossible());
    }

    @Test
    void stipendPossibilityTest2() {
        assertFalse(badStudentRecordBook.isStipendPossible());
    }

    @Test
    void averageMarkTest() {
        assertEquals(4.2, goodStudentRecordBook.getAverageMark());
    }

    @Test
    void redDiplomaTest1() {
        assertTrue(excellentStudentRecordBook.isRedDiploma());
    }

    @Test
    void redDiplomaTest2() {
        assertFalse(goodStudentRecordBook.isRedDiploma());
    }

    @Test
    void redDiplomaTest3() {
        assertFalse(badStudentRecordBook.isRedDiploma());
    }
}