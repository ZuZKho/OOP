package goncharov.checkers;

import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import java.util.HashMap;

/**
 * Interface for all checkers in application.
 */
public interface Checker {

    /**
     * Run checker.
     *
     * @param student student info.
     * @param task task info.
     * @param tasksResults place to store results.
     */
    void run(Student student, Task task, HashMap<Student, HashMap<Task, TaskResult>> tasksResults);
}

