import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void heapsort1() {
        Assertions.assertArrayEquals(new int[] {1, 2, 3, 6, 7}, Main.heapsort(new int[] {1, 3, 2, 7, 6}));
    }

    @Test
    void heapsort2() {
        Assertions.assertArrayEquals(new int[] {1, 2, 3, 6, 7}, Main.heapsort(new int[] {1, 3, 2, 7, 6}));
    }
}