package net.regsirius06.engine.API.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark the core of a plugin (mod) and specify its unique identifier.
 * It is required for all mod cores that implement the {@link net.regsirius06.engine.API.Core} interface.
 *
 * <p>The {@code ModId} annotation ensures that each mod core has a unique identifier, which is crucial for
 * the plugin system to correctly load and manage mods. The identifier is checked for uniqueness, and if any
 * mod core is annotated with a duplicate identifier, an {@link IllegalStateException} will be thrown.</p>
 *
 * <p>If a mod core is not annotated with this annotation, an {@link IllegalArgumentException} will be thrown.</p>
 *
 * @see net.regsirius06.engine.API.Core
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModId {
    /**
     * The unique identifier for the mod core.
     *
     * @return the mod's unique identifier
     */
    String value();
}
