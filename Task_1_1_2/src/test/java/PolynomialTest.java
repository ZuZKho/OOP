import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;


class PolynomialTest {

    @Test
    void plus() {
        Polynomial p1 = new Polynomial(new int[]{4, 3, 6, 7});
        Polynomial p2 = new Polynomial(new int[]{3, 2, 8});
        Assertions.assertEquals("7x^3 + 6x^2 + 19x + 6", p1.plus(p2.differentiate(1)).toString());
    }

    @Test
    void minus1() {
        Polynomial p1 = new Polynomial(new int[]{4, 3, 6, 7});
        Polynomial p2 = new Polynomial(new int[]{3, 2, 8});
        Assertions.assertEquals(p1.minus(p2),new Polynomial(new int[]{1, 1, -2, 7}));
    }

    @Test
    void minus2() {
        Polynomial p1 = new Polynomial(new int[]{4, 3, 6, 7});
        Polynomial p2 = new Polynomial(new int[]{3, 2, 8});
        Assertions.assertEquals((p1.minus(p1)).toString(), "0");
    }

    @Test
    void multiply() {
        Polynomial p1 = new Polynomial(new int[]{4, 3, 6, 7});
        Assertions.assertEquals((p1.multiply(new Polynomial(new int[]{0}))).toString(), "0");
    }

    @Test
    void evaluate() {
        Polynomial p1 = new Polynomial(new int[]{4, 3, 6, 7});
        Polynomial p2 = new Polynomial(new int[]{3, 2, 8});
        Assertions.assertEquals(3510, p1.multiply(p2).evaluate(2));
    }

    @ParameterizedTest
    @MethodSource("differentiateTestsProvider")
    void differentiateTests(int differentialDegree, Polynomial input, Polynomial expected) {
        Assertions.assertEquals(input.differentiate(differentialDegree), expected);
    }
    static Stream<Arguments> differentiateTestsProvider() {
        Polynomial p1 = new Polynomial(new int[]{1, 2, 3, 0, 4, 13});

        return Stream.of(
                Arguments.of(0, p1, p1),
                Arguments.of(1, p1, new Polynomial((new int[]{2, 6, 0, 16, 65}))),
                Arguments.of(2, p1, new Polynomial((new int[]{6, 0, 48, 260}))),
                Arguments.of(10, p1, new Polynomial(new int[]{0}))
        );
    }

    @Test
    void equals1() {
        Polynomial p1 = new Polynomial(new int[]{1, 2, 3, 0, 4, 13});
        Assertions.assertEquals(p1,new Polynomial(new int[]{1, 2, 3, 0, 4, 13}));
    }

    @Test
    void equals2() {
        Polynomial p1 = new Polynomial(new int[]{1, 2, 3, 0, 4, 13});
        Assertions.assertNotEquals(new Polynomial(new int[]{1, 2, 3, 0, 4, 12}), p1);
    }

    @Test
    void testToString1() {
        Assertions.assertEquals("5x^4 + 3x^2", (new Polynomial(new int[]{0, 0, 3, 0, 5})).toString());
    }

    @Test
    void testToString2() {
        Assertions.assertEquals("13x^5 + 4x^4 + 3x^2 + 2x + 1", (new Polynomial(new int[]{1, 2, 3, 0, 4, 13})).toString());
    }

    @Test
    void testEmpty() {
        Assertions.assertEquals("0", (new Polynomial(new int[]{})).toString());
    }
}
