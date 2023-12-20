import java.time.format.DateTimeFormatter;

/**
 * Класс для хранения общей информации: имя файла, формат даты в запросах.
 */
public class Shared {

    static public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm");
    static public final String fileName = "src/main/resources/notebook.json";

}
