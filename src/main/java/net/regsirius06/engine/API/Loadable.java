package net.regsirius06.engine.API;

/**
 * This marker interface is used to identify classes or interfaces that can be loaded into the system.
 *
 * <p>The {@code Loadable} interface does not define any methods and is not itself intended to be
 * directly loaded into the system. It serves as a marker for specific subclasses or interfaces
 * that are designed to be loaded as part of a plugin or modular architecture.</p>
 *
 * <p>Only specific interfaces that extend {@code Loadable} are eligible for loading into the system.
 * These include:</p>
 * <ul>
 *     <li>{@link Core}</li>
 *     <li>{@link Executable}</li>
 *     <li>{@link Initializer}</li>
 *     <li>{@link WallState}</li>
 * </ul>
 * <p>Any direct implementation of {@code Loadable} itself will not be loaded under any circumstances.</p>
 *
 * <p>Classes or interfaces extending these specific types are expected to include their own
 * loading behavior, which is handled by the system's dynamic loading mechanism.</p>
 *
 * @see Core
 * @see Executable
 * @see Initializer
 * @see WallState
 */
public interface Loadable {
}
