import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
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
    public void AddSemester(Semester semester) {
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
        Stream<Subject> subjectStream = Stream.empty();
        for (var semester : semesterList) {
            subjectStream = Stream.concat(subjectStream, semester.getSubjectList().stream());
        }
        return subjectStream;
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

        var lastMarkMap = getAllSubjectsStream().collect(Collectors.toMap(Subject::title, Subject::mark, (existing, replacement) -> replacement));

        int fourCount = 0;
        int fiveCount = 0;

        for (var subject : lastMarkMap.entrySet()) {
            switch (subject.getValue()) {
                case FOUR -> fourCount++;
                case FIVE -> fiveCount++;
                default -> {
                    return false;
                }
            }
        }

        return (double) fiveCount / (double) (fourCount + fiveCount) >= 0.75 && diplomaWork.mark() == Mark.FIVE;
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
