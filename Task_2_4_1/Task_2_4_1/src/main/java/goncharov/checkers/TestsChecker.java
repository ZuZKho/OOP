package goncharov.checkers;

import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * Checker that builds and running tests.
 */
public class TestsChecker implements Checker {

    private String storagePath;

    /**
     * Deafult constructor.
     *
     * @param storagePath path of repositories folder.
     */
    public TestsChecker(String storagePath) {
        this.storagePath = storagePath;
    }

    public void run(Student student, Task task, HashMap<Student, HashMap<Task, TaskResult>> tasksResults) {
        // Build
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(new File(storagePath + student.getNickname() + "/" + task.getTag()));
        ProjectConnection connection = connector.connect();
        try {
            connection.newBuild()
                    .forTasks("test")
                    .run();
        } catch (RuntimeException e) {
            System.out.println("Build exception: " + e);
            return;
        } finally {
            connection.close();
        }
        tasksResults.get(student).get(task).setCompiled(true);

        // Test results checking
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
        try {
            String testsResultsFileName = storagePath + student.getNickname() + "/" + task.getTag() + "/build/test-results/test/" + xmlName;
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(testsResultsFileName));
            doc.getDocumentElement().normalize();
            NamedNodeMap attrList = doc.getElementsByTagName("testsuite").item(0).getAttributes();
            int skipped = Integer.valueOf(attrList.getNamedItem("skipped").getNodeValue());
            int failed = Integer.valueOf(attrList.getNamedItem("failures").getNodeValue());
            int passed = Integer.valueOf(attrList.getNamedItem("tests").getNodeValue()) - skipped - failed;
            tasksResults.get(student).get(task).setTestsSkipped(skipped);
            tasksResults.get(student).get(task).setTestsPassed(passed);
            tasksResults.get(student).get(task).setTestsFailed(failed);
        } catch (Exception ignored) {
            System.out.println("Test results parse exception: " + ignored);
        }
    }
}
