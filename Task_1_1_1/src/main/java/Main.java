/**
 * Класс реализующий сортировку кучей.
 */
public class Main {

    /**
     * Функция, чтобы компиляция из shell скрипта не заканчивалась ошибкой
     * @param args флаги компиляции
     */
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }

    private static int sz;

    /**
     * Функция выполняющая сортировку массива.
     * Переданный в метод массив также отсортируется!!!
     *
     * @param arr массив который нужно отсортировать
     * @return отсортированный массив
     */
    public static int[] heapsort(int[] arr) {
        sz = arr.length;
        for (int i = sz / 2; i >= 0; i--) {
            siftDown(i, arr);
        }

        while (sz > 1) {
            // Делаем Extract
            int help = arr[0];
            arr[0] = arr[sz - 1];
            arr[sz - 1] = help;
            sz--;
            siftDown(0, arr);
        }

        return arr;
    }

    private static void siftDown(int idx, int[] arr) {
        if (idx * 2 + 1 < sz && arr[idx] < arr[idx * 2 + 1]
                || idx * 2 + 2 < sz && arr[idx] < arr[idx * 2 + 2]) {
            int toswp = idx * 2 + 1;
            if (idx * 2 + 2 < sz && arr[idx * 2 + 2] > arr[idx * 2 + 1]) {
                toswp = idx * 2 + 2;
            }
            // swap
            int help = arr[idx];
            arr[idx] = arr[toswp];
            arr[toswp] = help;

            siftDown(toswp, arr);
        }
    }

}
