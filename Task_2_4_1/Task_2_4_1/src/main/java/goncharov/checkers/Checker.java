package goncharov.checkers;

import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import java.util.HashMap;

public interface Checker {

    void run(Student student, Task task, HashMap<Student, HashMap<Task, TaskResult>> tasksResults);
}

