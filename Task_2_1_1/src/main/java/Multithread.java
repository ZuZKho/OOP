import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Многопточный класс на java.Thread.
 */
public class Multithread {

    /**
     * Возвращает содержит ли массив array составное число.
     *
     * @param array    long[], исходный массив.
     * @param nThreads количество потоков
     * @return true/false
     * @throws InterruptedException
     */
    public static boolean containsCompositeNumber(long[] array, int nThreads)
            throws InterruptedException {

        AtomicInteger idx = new AtomicInteger(0);
        AtomicBoolean contains = new AtomicBoolean(false);

        Thread[] threads = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(() -> {
                while (true) {
                    Integer cur = idx.getAndIncrement();
                    if (cur >= array.length) {
                        return;
                    }

                    if (!PrimeDetector.isPrime(array[cur])) {
                        contains.set(true);
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < nThreads; i++) {
            threads[i].join();
        }

        return contains.get();
    }

}
