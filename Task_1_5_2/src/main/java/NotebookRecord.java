import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Класс заметки в записной книжке.
 */
public class NotebookRecord {

    private final String title;
    private final String date;
    private final String content;

    @JsonCreator
    public NotebookRecord(@JsonProperty("title") String title, @JsonProperty("date") String date, @JsonProperty("content") String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public NotebookRecord(String title, String content) {
        this.title = title;
        this.date = LocalDateTime.now().format(Shared.formatter);
        this.content = content;
    }

    @JsonGetter
    public String title() {
        return this.title;
    }

    @JsonGetter
    public String date() {
        return this.date;
    }

    @JsonGetter
    public String content() {
        return this.content;
    }

    /**
     * Получить дату заметки в LocalDateTime.
     *
     * @return дата заметки.
     */
    public LocalDateTime dateDate() {
        return LocalDateTime.parse(this.date, Shared.formatter);
    }

    @Override
    public String toString() {
        return this.title + "\n" + this.date + "\n" + this.content + "\n";
    }
}
