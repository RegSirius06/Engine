package net.regsirius06.engine.API.annotations;

import net.regsirius06.engine.API.Loadable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark a class as an implementation of the {@link net.regsirius06.engine.API.Loadable} interface.
 *
 * <p>The {@code ImplementationOf} annotation is primarily used in the process of dependency file generation.
 * These files are used to load data from the plugin into the main application. If these files are manually
 * generated, the annotation is not required. However, if the dependency files are generated automatically,
 * this annotation is necessary to establish the relationship between the plugin and the {@link Loadable} interface.</p>
 *
 * @see net.regsirius06.engine.API.Loadable
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplementationOf {
    /**
     * Specifies the class that this plugin implements, which should extend {@link Loadable}.
     *
     * @return the {@link Loadable} class that this plugin implements
     */
    Class<? extends Loadable> value();
}
