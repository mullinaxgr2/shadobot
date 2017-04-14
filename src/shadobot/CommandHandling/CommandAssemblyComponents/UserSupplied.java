package shadobot.CommandHandling.CommandAssemblyComponents;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UserSupplied {
    String description() default "N/A";
}