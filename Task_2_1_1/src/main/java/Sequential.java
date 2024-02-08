/**
 * Класс последовательного вычисления.
 */
public class Sequential {

    /**
     * Возвращает содержит ли массив array составное число.
     *
     * @param array long[], исходный массив.
     * @return true/false
     */
    public static boolean containsCompositeNumber(long[] array) {
        for (int i = 0; i < array.length; i++) {
            if (!PrimeDetector.isPrime(array[i])) {
                return true;
            }
        }

        return false;
    }

}
