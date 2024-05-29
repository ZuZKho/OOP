package goncharov.dsl;

import lombok.Data;

@Data
public class TaskResult {

    public TaskResult(boolean downloaded) {
        this.downloaded = downloaded;
    }

    boolean downloaded = false;
    boolean compiled = false;
    boolean javadoc = false;
    boolean softDeadline = false;
    boolean hardDeadline = false;
    float mark = -0.5f;
    int checkstyleWarnings = -1;
    int testsPassed = -1;
    int testsSkipped = -1;
    int testsFailed = -1;
}
