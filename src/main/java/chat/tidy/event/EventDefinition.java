package chat.tidy.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventDefinition {

    EventType eventType();

    boolean isEnabled() default true;
}
