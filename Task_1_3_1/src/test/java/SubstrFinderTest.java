import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;


class SubstrFinderTest {


    @Test
    @DisplayName("Test with small buffer")
    void test1() {
        char[] substr = "aba".toCharArray();
        SubstrFinder substrFinder = new SubstrFinder(2);
        int[] expected = new int[]{0, 2, 4, 6, 8};

        try {
            int[] ans = substrFinder.find("src/test/java/input.txt", substr);
            assertArrayEquals(expected, ans);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    @DisplayName("Test with big buffer")
    void test2() {
        char[] substr = "aba".toCharArray();
        SubstrFinder substrFinder = new SubstrFinder();
        int[] expected = new int[]{0, 2, 4, 6, 8};

        try {
            int[] ans = substrFinder.find("src/test/java/input.txt", substr);
            assertArrayEquals(expected, ans);
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    @DisplayName("Test whole string is substring")
    void test3() {
        char[] substr = "abababababa".toCharArray();
        SubstrFinder substrFinder = new SubstrFinder();
        int[] expected = new int[]{0};

        try {
            int[] ans = substrFinder.find("src/test/java/input.txt", substr);
            assertArrayEquals(expected, ans);
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    @DisplayName("No substrings")
    void test4() {
        char[] substr = "brbrbr".toCharArray();
        SubstrFinder substrFinder = new SubstrFinder(2);
        int[] expected = new int[0];

        try {
            int[] ans = substrFinder.find("src/test/java/input.txt", substr);
            assertArrayEquals(expected, ans);
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    @DisplayName("No such file")
    void test5() {
        char[] substr = "brbrbr".toCharArray();
        SubstrFinder substrFinder = new SubstrFinder(2);

        assertThrows(FileNotFoundException.class, () -> substrFinder.find("src/test/java/input2.txt", substr));
    }

}