package goncharov.dsl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Student extends GroovyConfigurable {

    private String fullname;
    private String nickname;
    private String repository;
    private String group;
}