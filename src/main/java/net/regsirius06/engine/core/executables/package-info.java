/**
 * Provides implementations of the {@link net.regsirius06.engine.API.Executable} interface.
 *
 * <p>This package contains executable commands that can be invoked within the game engine.
 * The primary implementation in this package is {@link net.regsirius06.engine.core.executables.Help},
 * which extends {@link net.regsirius06.engine.utils.help.InfoExecutable} to provide core information
 * about the game engine.</p>
 *
 * <p>The classes in this package use the {@link net.regsirius06.engine.API.annotations.ImplementationOf}
 * annotation to indicate that they implement the {@code Executable} interface. This enables automatic
 * discovery and integration of these commands within the engine.</p>
 *
 * @see net.regsirius06.engine.API.Executable
 * @see net.regsirius06.engine.utils.help.InfoExecutable
 * @see net.regsirius06.engine.API.annotations.ImplementationOf
 */
package net.regsirius06.engine.core.executables;
