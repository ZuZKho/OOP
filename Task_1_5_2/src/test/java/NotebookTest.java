import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class NotebookTest {

    private static ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeAll
    static void beforeAll() {
        File file = new File(Shared.fileName);
        file.delete();
        System.setOut(new PrintStream(output));
    }

    /*
     * Сложная обработка времени, так как в функции -add использую .now() и чтобы затестить такое
     * замеряю время до вызова, после вызова и проверяю вложенность.
     */
    @Test
    @Order(1)
    void test1() {
        assertDoesNotThrow(() -> {
            final LocalDateTime before = LocalDateTime.parse(
                    LocalDateTime.now().format(Shared.formatter),
                    Shared.formatter);
            Main.main(new String[]{"-add", "Моя заметка", "Текст моей заметки"});
            final LocalDateTime after = LocalDateTime.parse(
                    LocalDateTime.now().format(Shared.formatter),
                    Shared.formatter);

            Main.main(new String[]{"-show"});

            String[] actual = output.toString().replace("\r", "").split("\n");
            output.reset();

            assertEquals("Моя заметка", actual[0]);
            assertEquals("Текст моей заметки", actual[2]);
            LocalDateTime actualTime = LocalDateTime.parse(actual[1], Shared.formatter);

            assertTrue(actualTime.isAfter(before)
                    && actualTime.isBefore(after)
                    || actualTime.equals(after)
                    || actualTime.equals(before));
        });
    }

    @Test
    @Order(2)
    void test2() {
        assertDoesNotThrow(() -> {
            Main.main(new String[]{"-show",
                LocalDateTime.now().minusHours(2).format(Shared.formatter),
                LocalDateTime.now().plusHours(2).format(Shared.formatter),
                "МоЯ"});

            String[] actual = output.toString().replace("\r", "").split("\n");
            output.reset();

            assertEquals("Моя заметка", actual[0]);
            assertEquals("Текст моей заметки", actual[2]);
        });
    }

    @Test
    @Order(3)
    void test3() {
        assertDoesNotThrow(() -> {
            Main.main(new String[]{"-show",
                LocalDateTime.now().minusHours(2).format(Shared.formatter),
                LocalDateTime.now().plusHours(2).format(Shared.formatter),
                "Твоя"});

            String actual = output.toString().replace("\r", "");

            assertEquals("Нет подходящих заметок\n", actual);
            output.reset();
        });
    }

    @Test
    @Order(4)
    void test4() {
        assertDoesNotThrow(() -> {
            Main.main(new String[]{"-rm", "Моя заметка"});
            Main.main(new String[]{"-show"});

            assertEquals("", output.toString());
            output.reset();
        });
    }

    @AfterAll
    public static void afterAll() {
        System.setOut(null);
    }
}