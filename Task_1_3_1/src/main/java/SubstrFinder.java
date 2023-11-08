import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Класс для буферизированного поиска подстроки.
 * имплементация: считывает буфер, ищет все подстроки которые ПОЛНОСТЬЮ ВОЙДУТ в этот буфер,
 * копирует конец буфера (который мы не успели обработать из за нашего ограничения), в начало,
 * считывает недостающие символы, чтобы буфер снова восполнился и повторяет.
 */
public class SubstrFinder {

    private final int minbufsz;

    /**
     * Constructor for predefined buffer size.
     */
    public SubstrFinder() {
        this.minbufsz = 1000;
    }

    /**
     * Constructor for custom buffer size.
     *
     * @param minbufsz minimal length of buffer.
     */
    public SubstrFinder(int minbufsz) {
        this.minbufsz = minbufsz;
    }

    /**
     * По строке возвращает массив ее z-Функции.
     *
     * @param string строка.
     * @return z-Функция.
     */
    private int[] getzFunction(String string) {
        String utf8String = new String(string.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        int len = utf8String.length();

        int[] zfunction = new int[len];
        zfunction[0] = 0;
        int l = 0, r = 0;

        // Посчитаем z-Функцию для искомой подстроки
        for (int i = 1; i < len; i++) {
            if (i <= r) {
                zfunction[i] = Math.min(zfunction[i - l], r - i + 1);
            }
            while (zfunction[i] + i < len && utf8String.charAt(zfunction[i]) == utf8String.charAt(zfunction[i] + i)) {
                zfunction[i]++;
            }
            if (i + zfunction[i] - 1 > r) {
                r = i + zfunction[i] - 1;
                l = i;
            }
        }

        return zfunction;
    }

    /**
     * Функция поиска подстроки в тексте, который расположен в файле.
     *
     * @param fileName имя файла в котором расположен текст.
     * @param substr искомая подстрока.
     * @return массив индексов вхождений.
     * @throws FileNotFoundException в случае если файл с таким именем не найден.
     */
    public int[] find(String fileName, String substr) throws FileNotFoundException {
        ArrayList<Integer> answer = new ArrayList<>();
        int substrLength = substr.length();
        int[] zfunction = getzFunction(substr);

        int bufferCapacity = Math.max(2 * substrLength, minbufsz);
        char[] buffer = new char[bufferCapacity];

        try (FileReader reader = new FileReader(fileName, StandardCharsets.UTF_8)) {
            int bufferLen = reader.read(buffer);
            int shift = 0; // количество символов уже прошедших через буфер и убранных из него
            int l = 0;
            int r = 0;

            do {
                // Обрабатываем z функцией подстроки, которые точно успеют закончиться в этом буфере
                for (int i = 0; i < bufferLen - substrLength + 1; i++) {
                    int realIdx = i + shift;
                    int curZ = 0;
                    if (realIdx < r) {
                        curZ = Math.min(zfunction[realIdx - l], r - realIdx + 1);
                    }
                    while (curZ < substrLength && buffer[i + curZ] == substr.charAt(curZ)) {
                        curZ++;
                    }
                    if (realIdx + curZ - 1 > r) {
                        r = realIdx + curZ - 1;
                        l = realIdx;
                    }
                    if (curZ == substrLength) {
                        answer.add(realIdx);
                    }
                }
                shift += bufferLen - substrLength + 1;

                // Перенесли последние substrLength - 1 байтов в начало
                System.arraycopy(buffer, bufferLen - substrLength + 1, buffer, 0, substrLength - 1);
                // Считываем еще кусок
                char[] tmp = new char[bufferCapacity - substrLength + 1];
                bufferLen = reader.read(tmp);
                // Если считывать нечего выходим
                if (bufferLen == -1) {
                    break;
                }
                // Копируем считанное в конец буфера
                System.arraycopy(tmp, 0, buffer, substrLength - 1, bufferLen);
                bufferLen += substrLength - 1;
            } while (true);

        } catch (IOException ex) {
            throw new FileNotFoundException("Can't find file");
        }

        return answer.stream().mapToInt(i -> i).toArray();
    }
}
