import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SubstrFinderTest {

    @Test
    @DisplayName("Test with small buffer")
    void test1() {
        String substr = new String("аба".getBytes(), StandardCharsets.UTF_8);
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
        String substr = new String("аба".getBytes(), StandardCharsets.UTF_8);
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
        String substr = new String("абабабабаба".getBytes(), StandardCharsets.UTF_8);
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
        String substr = new String("брбрбр".getBytes(), StandardCharsets.UTF_8);
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
        String substr = new String("брбрбр".getBytes(), StandardCharsets.UTF_8);
        SubstrFinder substrFinder = new SubstrFinder(2);

        assertThrows(FileNotFoundException.class, () -> substrFinder.find("src/test/java/input2.txt", substr));
    }

}