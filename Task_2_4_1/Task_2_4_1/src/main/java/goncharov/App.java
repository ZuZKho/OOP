package goncharov;

import goncharov.dsl.Config;
import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import com.puppycrawl.tools.checkstyle.Main;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;


public class App {

    private static HashMap<Student, HashMap<Task, TaskResult>> tasksResults = new HashMap<>();
    private static String storagePath = "src/main/resources/repos/";
    private static String configFileName = "src/main/resources/config.groovy";

    public static void main(String[] args) throws Exception {
        try {
            FileUtils.deleteDirectory(new File(storagePath));
        } catch (Exception e) {
            System.out.println(e);
        }

        Config config = parseConfiguration();

        Downloader downloader = new Downloader(storagePath);
        for (var student : config.getStudents()) {
            tasksResults.put(student, new HashMap<>());

            boolean downloaded = downloader.download(student.getRepository(), student.getNickname());
            for (var task : config.getTasks()) {
                tasksResults.get(student).put(task, new TaskResult(downloaded));
            }

            for (var task : config.getTasks()) {
                try {
                    runJavadoc(student, task);
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    runCheckstyle(student, task);
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    runBuild(student, task);
                    parseTestResults(student, task);
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    runDeadlinesCheck(student, task);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        try {
            Renderer.render(config.getTasks(), tasksResults);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static Config parseConfiguration() throws Exception {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        GroovyShell sh = new GroovyShell(App.class.getClassLoader(), new Binding(), cc);
        DelegatingScript script = (DelegatingScript)sh.parse(new File(configFileName));
        Config config = new Config();
        script.setDelegate(config);
        script.run();
        config.postProcess();
        return config;
    }

    private static void parseTestResults(Student student, Task task) throws Exception {
        String testResultsFolder = storagePath + student.getNickname() + "/" + task.getTag() + "/build/test-results/test/";
        File folder = new File(testResultsFolder);
        String xmlName = "";
        for (var file : folder.listFiles()) {
            String fileName = file.getName();
            if (fileName.endsWith(".xml") || fileName.endsWith(".XML")) {
                xmlName = fileName;
                break;
            }
        }

        String testsResultsFileName = storagePath + student.getNickname() + "/" + task.getTag() + "/build/test-results/test/" + xmlName;
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(testsResultsFileName));
        doc.getDocumentElement().normalize();
        NamedNodeMap attrList = doc.getElementsByTagName("testsuite").item(0).getAttributes();
        int skipped = Integer.valueOf(attrList.getNamedItem("skipped").getNodeValue());
        int failed =  Integer.valueOf(attrList.getNamedItem("failures").getNodeValue());
        int passed =  Integer.valueOf(attrList.getNamedItem("tests").getNodeValue()) - skipped - failed;
        tasksResults.get(student).get(task).setTestsSkipped(skipped);
        tasksResults.get(student).get(task).setTestsPassed(passed);
        tasksResults.get(student).get(task).setTestsFailed(failed);
    }

    private static void runBuild(Student student, Task task) {
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(new File(storagePath + student.getNickname() + "/" + task.getTag()));
        ProjectConnection connection = connector.connect();
        try {
            connection.newBuild()
                    .forTasks("test")
                    .run();
        } catch(RuntimeException e) {
            return;
        } finally {
            connection.close();
        }
        tasksResults.get(student).get(task).setCompiled(true);
    }

    private static void runJavadoc(Student student, Task task) {
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(new File(storagePath + student.getNickname() + "/" + task.getTag()));
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

    private static void runCheckstyle(Student student, Task task) throws Exception {
        // Checkstyle
        String checkstylePath = storagePath + "checkstyle.txt";
        catchSystemExit(() -> {
            String configPath = storagePath + "/" + student.getNickname() + "/.github/google_checks.xml";
            var mainSourcePath = storagePath + "/" + student.getNickname() + "/" + task.getTag() + "/src/main/java";
            Main.main("-c", configPath, "-o", checkstylePath, mainSourcePath);
        });
        BufferedReader reader = new BufferedReader(new FileReader(checkstylePath));
        int warningsCount = 0;
        while(reader.ready()) {
            if (reader.readLine().contains("[WARN]")) {
                warningsCount++;
            }
        }
        tasksResults.get(student).get(task).setCheckstyleWarnings(warningsCount);
        reader.close();
    }

    private static void runDeadlinesCheck(Student student, Task task) throws Exception {
        File repository = new File(storagePath + student.getNickname());
        var commits = Git.open(repository).log().addPath(task.getTag()).call();

        LocalDate firstCommitDate = null, lastCommitDate = null;

        for (RevCommit commit : commits) {
            LocalDate commitDate = LocalDate.ofInstant(Instant.ofEpochSecond(commit.getCommitTime()), ZoneId.systemDefault());
            if (firstCommitDate == null || commitDate.isBefore(firstCommitDate)) {
                firstCommitDate = commitDate;
            }
            if (lastCommitDate == null || commitDate.isAfter(lastCommitDate)) {
                lastCommitDate = commitDate;
            }
        }

        boolean softDeadline = firstCommitDate.isBefore(task.getSoftDeadline());
        boolean hardDeadline = lastCommitDate.isBefore(task.getHardDeadline());

        if (softDeadline && hardDeadline) {
            tasksResults.get(student).get(task).setMark("1.0");
        } else if (softDeadline && ! hardDeadline) {
            tasksResults.get(student).get(task).setMark("0.0");
        } else if (!softDeadline && hardDeadline) {
            tasksResults.get(student).get(task).setMark("0.5");
        }
        tasksResults.get(student).get(task).setSoftDeadline(softDeadline);
        tasksResults.get(student).get(task).setHardDeadline(hardDeadline);
        Git.shutdown();
    }
}