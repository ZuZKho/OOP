import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test class.
 * <p>
 * Time statistics:
 * ParallelStream: 52 ms
 * Sequential: 101 ms
 * Multithread (2 threads): 49 ms
 * Multithread (3 threads): 34 ms
 * Multithread (4 threads): 33 ms
 * Multithread (5 threads): 28 ms
 * Multithread (6 threads): 27 ms
 */
public class PrimeTest {

    final long[] array1 = new long[]{6, 8, 7, 13, 5, 9, 4};

    final long[] array2 = new long[]{
            20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053,
            1000000007, 1000000007, 1000000009, 1000000009, 10000000469L,
            999999999847L, 10000000469L, 999999999937L, 999999999959L, 999999999989L,
            999999999767L, 999999999767L, 100000009069L, 100000008937L, 100000008947L
    };

    @Test
    public void testSequential1() {
        assertTrue(Sequential.containsCompositeNumber(array1));
    }

    @Test
    public void testMultithread1() throws InterruptedException {
        assertTrue(Multithread.containsCompositeNumber(array1, 4));
    }

    @Test
    public void testParallel1() {
        assertTrue(ParallelStream.containsCompositeNumber(array1));
    }

    @Test
    public void testSequential2() {
        long begin = System.currentTimeMillis();
        boolean res = Sequential.containsCompositeNumber(array2);
        long end = System.currentTimeMillis();
        System.out.println("Sequential: " + (end - begin));

        assertFalse(res);
    }

    @Test
    public void testMultithread2() throws InterruptedException {
        for (int i = 2; i <= 6; i++) {
            boolean res = true;
            long begin = System.currentTimeMillis();
            res = Multithread.containsCompositeNumber(array2, i);
            long end = System.currentTimeMillis();
            System.out.println("Multithread (" + i + " threads): " + (end - begin));
            assertFalse(res);
        }
    }

    @Test
    public void testParallel2() {
        long begin = System.currentTimeMillis();
        boolean res = ParallelStream.containsCompositeNumber(array2);
        long end = System.currentTimeMillis();
        System.out.println("ParallelStream: " + (end - begin));

        assertFalse(res);
    }

}
