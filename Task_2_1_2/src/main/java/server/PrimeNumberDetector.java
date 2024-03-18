package server;

/**
 * Checking if number is prime.
 */
public class PrimeNumberDetector {

    /**
     * Checking if number is prime.
     *
     * @param x long value to check.
     * @return is x prime.
     */
    public static boolean isPrime(long x) {
        for (long i = 2; i * i <= x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

}
