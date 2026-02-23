package framework.annotations;
import java.lang.annotation.*;

@Target(ElementType.TYPE) // Works on Classes
@Retention(RetentionPolicy.RUNTIME) // Needed for Reflection
public @interface Component {
    String value() default "";
}