import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ExpressionCalculatorTest {

    private final double Eps = 0.00001;

    @Test
    @DisplayName("CommonCalculation")
    void test1() {
        String s = "+ / 1 2 / + 2 3 - * sin - 9 2 2 / 6 7";
        double expected = 11.444982;

        try {
            double actual = ExpressionCalculator.calculate(s);
            assertTrue(Math.abs(expected - actual) < Eps);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("CommonCalculation")
    void test2() {
        String s = "- * / 15 - 7 + 1 1 3 + 2 + 1 1";
        double expected = 5;

        try {
            double actual = ExpressionCalculator.calculate(s);
            assertTrue(Math.abs(expected - actual) < Eps);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("CommonCalculation")
    void test3() {
        //12^(log(11, sqrt(13 * cos (6 - 3)) ))
        String s = "pow 12 log 11 sqrt * 13 cos - 6 1";
        double expected = 1.96632367712742807;

        try {
            double actual = ExpressionCalculator.calculate(s);
            assertTrue(Math.abs(expected - actual) < Eps);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("Wrong prefix notation")
    void test4() {
        String s = "12 pow 12 log 11 sqrt * 13 cos - 6 1";

        assertThrows(WrongPrefixNotationException.class, () -> {
            ExpressionCalculator.calculate(s);
        });
    }

    @Test
    @DisplayName("Wrong prefix notation")
    void test5() {
        String s = "pow 12 log 11 sqrt * 13 cos - 6 + 1";

        assertThrows(WrongPrefixNotationException.class, () -> {
            ExpressionCalculator.calculate(s);
        });
    }

    @Test
    @DisplayName("Illegal prefix notation operand")
    void test6() {
        String s = "pow 12 log 11 sqr * 13 cos - 6 1";

        assertThrows(IllegalPrefixNotationOperand.class, () -> {
            ExpressionCalculator.calculate(s);
        });
    }

    @Test
    @DisplayName("Illegal function argument")
    void test7() {
        String s = "log 1 2";

        assertThrows(IllegalFunctionArgument.class, () -> {
            ExpressionCalculator.calculate(s);
        });
    }

    @Test
    @DisplayName("Illegal function argument")
    void test8() {
        String s = "/ 2 sin 0";

        assertThrows(IllegalFunctionArgument.class, () -> {
            ExpressionCalculator.calculate(s);
        });
    }
}