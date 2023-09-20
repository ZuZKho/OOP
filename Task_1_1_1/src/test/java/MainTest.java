import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class MainTest {

    @Test
    void heapsort1() {
        int[] ans = Main.heapsort(new int[] {1, 3, 2, 7, 6});
        Assertions.assertArrayEquals(new int[] {1, 2, 3, 6, 7}, ans);
    }

    @Test
    void heapsort2() {
        Assertions.assertArrayEquals(new int[] {-7, -3, 1, 2, 6, 11, 15, 19, 23},
                Main.heapsort(new int[] {1, -3, 2, - 7, 11, 23, 15, 19, 6}));
    }

    @Test
    void heapsortSingleton() {
        int[] ans = Main.heapsort(new int[] {1});
        Assertions.assertArrayEquals(new int[] {1}, ans);
    }

    @Test
    void heapsortEmpty() {
        Assertions.assertArrayEquals(new int[] {},
                Main.heapsort(new int[] {}));
    }

    @Test
    void heapsortLong() {
        final int Maxn = 1000_000;
        int[] arr = new int[Maxn];
        int[] ans = new int[Maxn];
        for (int i = 0; i < Maxn; i++) {
            arr[i] = ThreadLocalRandom.current().nextInt(-1000_000_000, 1000_000_000);
            ans[i] = arr[i];
        }

        Arrays.sort(ans);
        Assertions.assertArrayEquals(ans, Main.heapsort(arr));
    }

}