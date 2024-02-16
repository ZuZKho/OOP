import java.util.Arrays;

/**
 * Многопоточный класс на ParallelStream.
 */
public class ParallelStream {

    /**
     * Возвращает содержит ли массив array составное число.
     *
     * @param array long[], исходный массив.
     * @return true/false.
     */
    public static boolean containsCompositeNumber(long[] array) {
        return Arrays.stream(array).parallel().anyMatch(number -> !PrimeDetector.isPrime(number));
    }

}
