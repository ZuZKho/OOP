package goncharov;

import freemarker.template.Configuration;
import freemarker.template.Template;
import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {

    public static void render(List<Task> tasks, HashMap<Student, HashMap<Task, TaskResult>> tasksResults) {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
            cfg.setClassForTemplateLoading(Renderer.class, "/");
            cfg.setDefaultEncoding("UTF-8");
            Map<String, Object> root = new HashMap<>();
            root.put("tasks", tasks);
            root.put("tasksResults", tasksResults);

            HashMap<Student, String> studentsMarks = new HashMap<>();
            for (var entry : tasksResults.entrySet()) {
                String mark = entry.getValue().values().stream().map(result -> result.getMark()).reduce(0.0f, Float::sum).toString();

                studentsMarks.put(entry.getKey(), mark);
            }

            root.put("studentsMarks", studentsMarks);

            Template temp = cfg.getTemplate("template.ftl");
            Writer out = new OutputStreamWriter(new FileOutputStream("src/main/resources/index.html"));
            temp.process(root, out);
        } catch (Exception e) {
            System.out.println("Can't render results: " + e);
        }
    }
}
