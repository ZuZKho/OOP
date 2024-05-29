package goncharov.dsl;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Task info class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Task extends GroovyConfigurable {
    String tag;
    LocalDate softDeadline;
    LocalDate hardDeadline;
}
