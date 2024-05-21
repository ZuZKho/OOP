package goncharov;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.Configuration;
import goncharov.dsl.Student;
import goncharov.dsl.Task;
import goncharov.dsl.TaskResult;

public class Renderer {

    public static void render(List<Task> tasks, HashMap<Student, HashMap<Task, TaskResult>> tasksResults) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(Renderer.class, "/");
        cfg.setDefaultEncoding("UTF-8");
        Map<String, Object> root = new HashMap<>();
        root.put("tasks", tasks);
        root.put("tasksResults", tasksResults);

        Template temp = cfg.getTemplate("template.ftl");
        Writer out = new OutputStreamWriter(new FileOutputStream("src/main/resources/index.html"));
        temp.process(root, out);
    }
}
