import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    @Test
    void plus() {
        Polynomial p1 = new Polynomial(new int[] {4, 3, 6, 7});
        Polynomial p2 = new Polynomial(new int[] {3, 2, 8});
        Assertions.assertEquals("7x^3 + 6x^2 + 19x + 6", p1.plus(p2.differentiate(1)).toString());
    }

    @Test
    void minus() {
        Polynomial p1 = new Polynomial(new int[] {4, 3, 6, 7});
        Polynomial p2 = new Polynomial(new int[] {3, 2, 8});
        Assertions.assertTrue(p1.minus(p2).isEqual(new Polynomial(new int[]{1, 1, -2, 7})));
        Assertions.assertEquals((p1.minus(p1)).toString(), "0");
    }

    @Test
    void times() {
        Polynomial p1 = new Polynomial(new int[] {4, 3, 6, 7});
        Assertions.assertEquals((p1.times(new Polynomial(new int[]{0}))).toString(), "0");
    }

    @Test
    void evaluate() {
        Polynomial p1 = new Polynomial(new int[] {4, 3, 6, 7});
        Polynomial p2 = new Polynomial(new int[] {3, 2, 8});
        Assertions.assertEquals(3510, p1.times(p2).evaluate(2));
    }

    @Test
    void differentiate() {
        Polynomial p1 = new Polynomial(new int[]{1, 2, 3, 0, 4, 13});
        Assertions.assertTrue(p1.differentiate(0).isEqual(new Polynomial((new int[]{1, 2, 3, 0, 4, 13}))));
        Assertions.assertTrue(p1.differentiate(1).isEqual(new Polynomial((new int[]{2, 6, 0, 16, 65}))));
        Assertions.assertTrue(p1.differentiate(2).isEqual(new Polynomial((new int[]{6, 0, 48, 260}))));
        Assertions.assertTrue(p1.differentiate(10).isEqual(new Polynomial(new int[]{0})));
    }

    @Test
    void isEqual() {
        Polynomial p1 = new Polynomial(new int[]{1, 2, 3, 0, 4, 13});
        Assertions.assertTrue(p1.isEqual(new Polynomial(new int[]{1, 2, 3, 0, 4, 13})));
        Assertions.assertFalse((new Polynomial(new int[]{1, 2, 3, 0, 4, 12})).isEqual(p1));
    }

    @Test
    void testToString() {
        Assertions.assertEquals("13x^5 + 4x^4 + 3x^2 + 2x + 1", (new Polynomial(new int[]{1, 2, 3, 0, 4, 13})).toString());
        Assertions.assertEquals("5x^4 + 3x^2", (new Polynomial(new int[]{0, 0, 3, 0, 5})).toString());
    }
}