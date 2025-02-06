package net.regsirius06.engine.API.annotations;

import net.regsirius06.engine.API.Core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to integrate functionality from one plugin into another.
 *
 * <p>When a class is annotated with {@code @AddTo}, it becomes available not only in the plugin
 * it belongs to but also in the plugin specified by the value of the annotation. The plugin name
 * is derived from the {@link ModId} annotation of the target plugin.</p>
 *
 * <p>If the value specified is {@code "Base"}, the annotated class is integrated directly into
 * the core of the application (the "Base" plugin). This is a common use case when you want to add
 * functionality to the core system, such as adding components or features to the {@link Core} interface
 * or other parts of the system.</p>
 *
 * <p>It is important to note that the {@code "Base"} core should not be annotated with this annotation,
 * as it is the core system itself, and cannot be extended or modified directly via {@code @AddTo}.</p>
 *
 * <p>The name of the core system ("Base") can be retrieved dynamically using the
 * {@link Core#getName()} method.</p>
 *
 * <p>For example, a class annotated with {@code @AddTo("Base")} could add functionality to core
 * components like {@link Core#getExecutables()}, {@link Core#getLabyrinthPanels()}, or other components
 * based on the plugin's design.</p>
 *
 * <p>The {@code AddTo} annotation enables seamless integration of one plugin's functionality into
 * another, facilitating inter-plugin interaction or extending core system features.</p>
 *
 * @see ModId
 * @see Core
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AddTo {
    /**
     * The name of the plugin or core system to which the annotated class should be added.
     *
     * @return the name of the target plugin or core system (e.g., "Base" for the core)
     */
    String value();
}
