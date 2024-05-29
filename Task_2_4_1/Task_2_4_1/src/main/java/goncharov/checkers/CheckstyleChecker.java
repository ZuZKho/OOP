package goncharov.checkers;

import com.puppycrawl.tools.checkstyle.Main;
import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;

/**
 * Checker that runs checkstyle.
 */
public class CheckstyleChecker implements Checker {

    private String storagePath;

    /**
     * Deafult constructor.
     *
     * @param storagePath path of repositories folder.
     */
    public CheckstyleChecker(String storagePath) {
        this.storagePath = storagePath;
    }

    public void run(Student student, Task task, HashMap<Student, HashMap<Task, TaskResult>> tasksResults) {
        try {
            String checkstylePath = storagePath + "checkstyle.txt";
            catchSystemExit(() -> {
                String configPath = storagePath + "/" + student.getNickname() + "/.github/google_checks.xml";
                var mainSourcePath = storagePath + "/" + student.getNickname() + "/" + task.getTag() + "/src/main/java";
                Main.main("-c", configPath, "-o", checkstylePath, mainSourcePath);
            });
            BufferedReader reader = new BufferedReader(new FileReader(checkstylePath));
            int warningsCount = 0;
            while (reader.ready()) {
                if (reader.readLine().contains("[WARN]")) {
                    warningsCount++;
                }
            }
            tasksResults.get(student).get(task).setCheckstyleWarnings(warningsCount);
            reader.close();
        } catch (Exception ignored) {
        }
    }
}
