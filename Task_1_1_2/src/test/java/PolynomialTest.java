import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    @Test
    void plus() {
    }

    @Test
    void minus() {
    }

    @Test
    void times() {
    }

    @Test
    void evaluate() {
    }

    @Test
    void differentiate() {
    }

    @Test
    void isEqual() {
        Assertions.assertEquals("13x^5 + 4x^4 + 3x^2 + 2x + 1", (new Polynomial(new int[]{1, 2, 3, 0, 4, 13})).toString());
        Assertions.assertEquals("5x^4 + 3x^2", (new Polynomial(new int[]{0, 0, 3, 0, 5})).toString());
    }

    @Test
    void testToString() {
        Assertions.assertEquals("13x^5 + 4x^4 + 3x^2 + 2x + 1", (new Polynomial(new int[]{1, 2, 3, 0, 4, 13})).toString());
        Assertions.assertEquals("5x^4 + 3x^2", (new Polynomial(new int[]{0, 0, 3, 0, 5})).toString());
    }
}