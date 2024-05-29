package goncharov.dsl;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * App configuration class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Config extends GroovyConfigurable {
    private List<Task> tasks;
    private List<Student> students;
}
