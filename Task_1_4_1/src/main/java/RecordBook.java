import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecordBook {

    private final List<Semester> semesterList = new ArrayList<>();
    private Subject diplomaWork;

    /**
     * Добавить семестр с оценками.
     *
     * @param semester добавляемый семестр.
     */
    public void addSemester(Semester semester) {
        semesterList.add(semester);
    }

    /**
     * Получить объект последнего семестра.
     *
     * @return объект последнего семестра, в котором можно изменять оценки.
     */
    public Semester getLastSemester() {
        if (semesterList.isEmpty()) {
            return null;
        }

        return semesterList.get(semesterList.size() - 1);
    }

    /**
     * Установить оценку для дипломной работы.
     *
     * @param subject дипломная работа.
     */
    public void setDiplomaWork(Subject subject) {
        diplomaWork = subject;
    }

    private Stream<Subject> getAllSubjectsStream() {
        return semesterList.stream().flatMap(i -> i.getSubjectList().stream());
    }

    /**
     * Получить среднюю оценку за все предметы всех семестров.
     *
     * @return средняя оценка.
     */
    public Double getAverageMark() {
        OptionalDouble result = getAllSubjectsStream()
                .mapToInt(i -> i.mark().value)
                .average();

        if (result.isPresent()) {
            return result.getAsDouble();
        } else {
            return null;
        }
    }

    /**
     * Возможно ли получить красный диплом.
     *
     * @return возможно ли?
     */
    public boolean isRedDiploma() {
        if (semesterList.isEmpty() || diplomaWork == null) {
            return false;
        }

        var lastMarkMap = getAllSubjectsStream()
                .collect(Collectors.toMap(
                        Subject::title,
                        Subject::mark,
                        (existing, replacement) -> replacement
                ));

        Map<Mark, Long> count = lastMarkMap.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long fourCount = count.getOrDefault(Mark.FOUR, 0L);
        long fiveCount = count.getOrDefault(Mark.FIVE, 0L);

        return fourCount + fiveCount == count.values().stream().reduce((long) 0, Long::sum)
                && fourCount + fiveCount != 0
                && (double) fiveCount / (double) (fourCount + fiveCount) >= 0.75
                && diplomaWork.mark() == Mark.FIVE;
    }

    /**
     * Возможно ли получить повышенную стипендию.
     * На это влияет только последний семестр.
     *
     * @return возможно ли?
     */
    public boolean isStipendPossible() {
        if (semesterList.isEmpty()) {
            return false;
        }

        return getLastSemester().isStipendPossible();
    }
}
