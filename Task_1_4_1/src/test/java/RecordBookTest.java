import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RecordBookTest {

    static RecordBook badStudentRB = new RecordBook();
    static RecordBook goodStudentRB = new RecordBook();
    static RecordBook excellentStudentRB = new RecordBook();

    @BeforeAll
    static void setBadStudentRB() {
        Semester semester = new Semester();
        semester.addSubject(new Subject("Math", Mark.THREE));
        semester.addSubject(new Subject("Haskell", Mark.FOUR));
        badStudentRB.addSemester(semester);
    }

    @BeforeAll
    static void setGoodStudentRB() {
        Semester firstSemester = new Semester();
        firstSemester.addSubject(new Subject("Math", Mark.THREE));
        firstSemester.addSubject(new Subject("Haskell", Mark.FOUR));
        goodStudentRB.addSemester(firstSemester);


        Semester secondSemester = new Semester();
        secondSemester.addSubject(new Subject("Math", Mark.FOUR));
        goodStudentRB.addSemester(secondSemester);
        secondSemester = goodStudentRB.getLastSemester();
        secondSemester.addSubject(new Subject("OOP", Mark.FIVE));
        secondSemester.addSubject(new Subject("Programming", Mark.FIVE));

        goodStudentRB.setDiplomaWork(new Subject("Differences between Imperative and Declarative programming", Mark.FIVE));
    }

    @BeforeAll
    static void setExcellentStudentRB() {
        Semester firstSemester = new Semester();
        firstSemester.addSubject(new Subject("Math", Mark.FOUR));
        firstSemester.addSubject(new Subject("Haskell", Mark.FOUR));
        excellentStudentRB.addSemester(firstSemester);

        Semester secondSemester = new Semester();
        secondSemester.addSubject(new Subject("Math", Mark.FIVE));
        secondSemester.addSubject(new Subject("OOP", Mark.FIVE));
        secondSemester.addSubject(new Subject("Project", Mark.FIVE));
        secondSemester.addSubject(new Subject("Operating Systems", Mark.FIVE));
        excellentStudentRB.addSemester(secondSemester);

        excellentStudentRB.setDiplomaWork(
                new Subject(
                        "Differences between Imperative and Declarative programming",
                        Mark.FIVE
                ));
    }

    @Test
    void stipendPossibilityTest1() {
        assertTrue(excellentStudentRB.isStipendPossible());
    }

    @Test
    void stipendPossibilityTest2() {
        assertFalse(badStudentRB.isStipendPossible());
    }

    @Test
    void averageMarkTest() {
        assertEquals(4.2, goodStudentRB.getAverageMark());
    }

    @Test
    void redDiplomaTest1() {
        assertTrue(excellentStudentRB.isRedDiploma());
    }

    @Test
    void redDiplomaTest2() {
        assertFalse(goodStudentRB.isRedDiploma());
    }

    @Test
    void redDiplomaTest3() {
        assertFalse(badStudentRB.isRedDiploma());
    }
}