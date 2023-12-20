import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Notebook {

    private final File file = new File(Shared.fileName);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Функция для чтения заметок из JSON.
     *
     * @return Список заметок.
     * @throws IOException Ошибка чтения из файла или распознавания JSON'а.
     */
    private List<NotebookRecord> readJson() throws IOException {
        List<NotebookRecord> list = objectMapper.readValue(file, new TypeReference<>() {
        });
        return list;
    }

    /**
     * Конструктор по умолчанию. Создает файл JSON'а при его отсутствии.
     */
    public Notebook() {
        file.getParentFile().mkdirs();
        try {
            if (file.createNewFile()) {
                objectMapper.writeValue(file, new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Функция, записывающая список заметок в JSON.
     *
     * @param records список заметок.
     * @throws IOException ошибка записи в файл.
     */
    private void writeJson(List<NotebookRecord> records) throws IOException {
        objectMapper.writeValue(file, records);
    }

    /**
     * Вывести в консоль список заметок.
     *
     * @param records список заметок.
     */
    private void render(List<NotebookRecord> records) {
        for (var record : records) {
            System.out.println(record);
        }
    }

    /**
     * Добавить новую заметку.
     *
     * @param record заметка.
     */
    public void add(NotebookRecord record) {
        try {
            List<NotebookRecord> records = readJson();
            records.add(record);
            writeJson(records);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Удалить заметку по заголовку.
     *
     * @param title заголовок заметки.
     */
    public void remove(String title) {
        try {
            List<NotebookRecord> records = readJson();
            writeJson(records.stream().filter(record -> !title.equals(record.title())).toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Показать все заметки.
     */
    public void show() {
        try {
            List<NotebookRecord> records = readJson();
            render(records);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Показать заметки удовлетворяющие фильтру.
     *
     * @param startDateString левая граница даты.
     * @param endDateString   правая граница даты.
     * @param keywords        ключевые слова в заголовке.
     */
    public void show(String startDateString, String endDateString, String[] keywords) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(startDateString, Shared.formatter);
            endDate = LocalDateTime.parse(endDateString, Shared.formatter);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка парсинга даты");
        }

        try {
            List<NotebookRecord> records = readJson();

            var filteredRecords = records.stream()
                    .filter(record -> record.dateDate().isBefore(endDate)
                            && record.dateDate().isAfter(startDate)
                            || record.dateDate().equals(startDate)
                            || record.dateDate().equals(endDate))
                    .filter(record -> {
                        for (var keyword : keywords) {
                            String title = record.title().toLowerCase();
                            if (title.contains(keyword)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .toList();

            if (filteredRecords.isEmpty()) {
                System.out.println("Нет подходящих заметок");
            } else {
                render(filteredRecords);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
