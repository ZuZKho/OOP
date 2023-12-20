import java.time.format.DateTimeFormatter;

/**
 * Класс для хранения общей информации: имя файла, формат даты в запросах.
 */
public class Shared {

    public static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm");
    public static final String fileName = "src/main/resources/notebook.json";

}
