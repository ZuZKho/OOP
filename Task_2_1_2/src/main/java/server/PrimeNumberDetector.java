package server;

public class PrimeNumberDetector {

    public static boolean isPrime(long x) {
        for (long i = 2; i * i <= x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

}
