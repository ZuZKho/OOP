package goncharov.checkers;

import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * Checker for checking commits dates.
 */
public class DeadlinesChecker implements Checker {

    private String storagePath;

    /**
     * Deafult constructor.
     *
     * @param storagePath path of repositories folder.
     */
    public DeadlinesChecker(String storagePath) {
        this.storagePath = storagePath;
    }

    /**
     * Run deadlines checker.
     *
     * @param student student info.
     * @param task task info.
     * @param tasksResults place to store results.
     */
    public void run(Student student, Task task, HashMap<Student,
                            HashMap<Task, TaskResult>> tasksResults) {
        try {
            File repository = new File(storagePath + student.getNickname());
            var commits = Git.open(repository).log().addPath(task.getTag()).call();

            LocalDate firstCommitDate = null;
            LocalDate lastCommitDate = null;

            for (RevCommit commit : commits) {
                LocalDate commitDate = LocalDate.ofInstant(
                        Instant.ofEpochSecond(commit.getCommitTime()), ZoneId.systemDefault());
                if (firstCommitDate == null || commitDate.isBefore(firstCommitDate)) {
                    firstCommitDate = commitDate;
                }
                if (lastCommitDate == null || commitDate.isAfter(lastCommitDate)) {
                    lastCommitDate = commitDate;
                }
            }

            boolean softDeadline = firstCommitDate.isBefore(task.getSoftDeadline());
            boolean hardDeadline = lastCommitDate.isBefore(task.getHardDeadline());

            tasksResults.get(student).get(task).setSoftDeadline(softDeadline);
            tasksResults.get(student).get(task).setHardDeadline(hardDeadline);
            Git.shutdown();
        } catch (Exception ignored) {
        }
    }
}
