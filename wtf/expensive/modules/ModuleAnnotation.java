package wtf.expensive.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface ModuleAnnotation {
    String name();
    String desc() default "";
    Type type();
}
