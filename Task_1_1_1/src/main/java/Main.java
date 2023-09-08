import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static int sz;

    public static int[] heapsort(int[] arr) {
        sz = arr.length;
        for(int i = 1; i < sz; i++) {
            Sift_Up(i, arr);
        }

        while(sz > 1) {
            // Делаем Extract
            int help = arr[0];
            arr[0] = arr[sz - 1];
            arr[sz - 1] = help;
            sz --;
            Sift_Down(0, arr);
        }

        return arr;
    }

    private static void Sift_Up(int idx, int[] arr) {
        if (idx == 0) return;

        int par = (idx - 1) / 2;
        System.out.println(par);
        if (arr[par] < arr[idx]) {
            //swap
            int help = arr[par];
            arr[par] = arr[idx];
            arr[idx] = help;

            Sift_Up(par, arr);
        }
    }

    private static void Sift_Down(int idx, int[] arr) {
        if (idx * 2 + 1 < sz && arr[idx] < arr[idx * 2 + 1] || idx * 2 + 2 < sz && arr[idx] < arr[idx * 2 + 2]     ) {
            int toswp = idx * 2 + 1;
            if (idx * 2 + 2 < sz && arr[idx * 2 + 2] > arr[idx * 2 + 1]) toswp = idx * 2 + 2;

            // swap
            int help = arr[idx];
            arr[idx] = arr[toswp];
            arr[toswp] = help;

            Sift_Down(toswp, arr);
        }
    }

}
