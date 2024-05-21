package goncharov.dsl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class Config extends GroovyConfigurable {
    private List<Task> tasks;
    private List<Student> students;
}
