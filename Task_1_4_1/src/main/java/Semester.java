import java.util.ArrayList;
import java.util.List;

public class Semester {

    List<Subject> subjectList = new ArrayList<>();

    /**
     * Добавить предмет в семестр.
     *
     * @param subject предмет.
     */
    public void addSubject(Subject subject) {
        subjectList.add(subject);
    }

    /**
     * Получить список всех предметов в семестре.
     *
     * @return список предметов.
     */
    public List<Subject> getSubjectList() {
        return subjectList;
    }

    /**
     * Возможна ли стипендия?
     * Не должно быть троек.
     *
     * @return возможна ли?
     */
    public boolean isStipendPossible() {
        return subjectList.stream().noneMatch(i -> i.mark().value <= 3);
    }
}
