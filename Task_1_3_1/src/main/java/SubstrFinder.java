import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SubstrFinder {

    private final int minbufsz;

    public SubstrFinder() {
        this.minbufsz = 1000;
    }

    public SubstrFinder(int minbufsz) {
        this.minbufsz = minbufsz;
    }

    private int[] getZFunction(char[] string) {
        int len = string.length;

        int[] zFunction = new int[len];
        zFunction[0] = 0;
        int l = 0, r = 0;

        // Посчитаем z-Функцию для искомой подстроки
        for (int i = 1; i < len; i++) {
            if (i <= r) {
                zFunction[i] = Math.min(zFunction[i - l], r - i + 1);
            }
            while (zFunction[i] + i < len && string[zFunction[i]] == string[zFunction[i] + i]) {
                zFunction[i]++;
            }
            if (i + zFunction[i] - 1 > r) {
                r = i + zFunction[i] - 1;
                l = i;
            }
        }

        return zFunction;
    }

    public int[] find(String fileName, char[] substr) throws FileNotFoundException {
        ArrayList<Integer> answer = new ArrayList<>(); 
        int substrLength = substr.length;
        int[] zFunction = getZFunction(substr);

        int bufferCapacity = Math.max(2 * substrLength, minbufsz);
        char[] buffer = new char[bufferCapacity];

        try (FileReader reader = new FileReader(fileName)) {
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
                        curZ = Math.min(zFunction[realIdx - l], r - realIdx + 1);
                    }
                    while (curZ < substrLength && buffer[i + curZ] == substr[curZ]) {
                        curZ++;
                    }
                    if (realIdx + curZ - 1 > r) {
                        r = realIdx + curZ - 1;
                        l = realIdx;
                    }
                    if (curZ == substrLength) answer.add(realIdx);
                }
                shift += bufferLen - substrLength + 1;

                // Перенесли последние substrLength - 1 байтов в начало
                System.arraycopy(buffer, bufferLen - substrLength + 1, buffer, 0, substrLength - 1);
                // Считываем еще кусок
                char[] tmp = new char[bufferCapacity - substrLength + 1];
                bufferLen = reader.read(tmp);
                // Если считывать нечего выходим
                if (bufferLen == -1) break;
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