package wtf.expensive.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface CommandAnnotation {
    String name();
    String description();
}
