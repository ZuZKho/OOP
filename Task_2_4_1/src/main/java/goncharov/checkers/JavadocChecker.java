package goncharov.checkers;

import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import java.io.File;
import java.util.HashMap;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

/**
 * Checker that runs javadoc task.
 */
public class JavadocChecker implements Checker {

    private String storagePath;

    /**
     * Deafult constructor.
     *
     * @param storagePath path of repositories folder.
     */
    public JavadocChecker(String storagePath) {
        this.storagePath = storagePath;
    }

    /**
     * Run javadoc checker.
     *
     * @param student student info.
     * @param task task info.
     * @param tasksResults place to store results.
     */
    public void run(Student student, Task task, HashMap<Student, HashMap<Task, TaskResult>> tasksResults) {
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(new File(storagePath + student.getNickname()
                                                                        + "/" + task.getTag()));
        ProjectConnection connection = connector.connect();
        try {
            connection.newBuild()
                    .forTasks("javadoc")
                    .run();
        } catch (RuntimeException e) {
            return;
        } finally {
            connection.close();
        }
        tasksResults.get(student).get(task).setJavadoc(true);
    }

}
