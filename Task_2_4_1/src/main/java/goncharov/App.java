package goncharov;

import goncharov.checkers.*;
import goncharov.dsl.Config;
import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * Main App class.
 */
public class App {

    private static HashMap<Student, HashMap<Task, TaskResult>> tasksResults = new HashMap<>();
    private static String storagePath = "src/main/resources/repos/";
    private static String configFileName = "src/main/resources/config.groovy";
    private static Downloader downloader = new Downloader(storagePath);
    private static Checker[] checkers = new Checker[]{new JavadocChecker(storagePath), new CheckstyleChecker(storagePath), new DeadlinesChecker(storagePath), new TestsChecker(storagePath)};

    /**
     * App starting method.
     *
     * @param args not used.
     * @throws Exception any problems during deleting files.
     */
    public static void main(String[] args) throws IOException {
        // Initializing App
        Config config;
        try {
            config = parseConfiguration();
        } catch (Exception e) {
            System.out.println("Can't parse config file: " + e);
            return;
        }
        FileUtils.deleteDirectory(new File(storagePath));

        for (var student : config.getStudents()) {
            tasksResults.put(student, new HashMap<>());

            // Downloading repositories
            boolean downloaded = downloader.download(student.getRepository(), student.getNickname());
            for (var task : config.getTasks()) {
                tasksResults.get(student).put(task, new TaskResult(downloaded));
            }
            if (!downloaded) {
                continue;
            }

            //  Run all needed checks
            for (var task : config.getTasks()) {
                for (var checker : checkers) {
                    checker.run(student, task, tasksResults);
                }
            }
        }

        calculateMarks();

        // Render results
        Renderer.render(config.getTasks(), tasksResults);
    }

    /**
     * Groovy configuration parser.
     *
     * @return Config of App.
     * @throws Exception any parsing problems.
     */
    private static Config parseConfiguration() throws Exception {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        GroovyShell sh = new GroovyShell(App.class.getClassLoader(), new Binding(), cc);
        DelegatingScript script = (DelegatingScript) sh.parse(new File(configFileName));
        Config config = new Config();
        script.setDelegate(config);
        script.run();
        config.postProcess();
        return config;
    }

    /**
     * Calculating marks on tasksResults HashMap.
     */
    public static void calculateMarks() {
        tasksResults.values().forEach(studentResult -> studentResult.values().forEach(result -> {
            if (result.isCompiled()
                    && result.isJavadoc()
                    && result.getCheckstyleWarnings() <= 9
                    && result.getTestsFailed() == 0
                    && result.getTestsSkipped() == 0) {
                if (result.isSoftDeadline() && result.isHardDeadline()) {
                    result.setMark(1);
                } else if (result.isSoftDeadline() && !result.isHardDeadline()) {
                    result.setMark(0);
                } else if (!result.isSoftDeadline() && result.isHardDeadline()) {
                    result.setMark(0.5f);
                }
            }
        }));
    }
}