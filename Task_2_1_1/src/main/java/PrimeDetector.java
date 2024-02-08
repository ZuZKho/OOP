/**
 * Utility class простых чисел.
 */
public class PrimeDetector {

    /**
     * Определение простоты числа.
     *
     * @param number тестируемое число.
     * @return является ли число простым.
     */
    public static boolean isPrime(long number) {

        for(long i = 2; i * i <= number; i++) {
            if (number % i == 0) return false;
        }

        return true;
    }

}
