// framework/annotations/Qualifier.java
package net.hajar.ioc_projects.framework.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
public @interface Qualifier {
    String value();
}