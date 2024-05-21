package goncharov.dsl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper=false)
public class Task extends GroovyConfigurable {
    String tag;
    LocalDate softDeadline;
    LocalDate hardDeadline;
}
