/**
 * Provides the core interfaces and abstract classes for the plugin system.
 *
 * <p>This package includes the primary abstractions that define the structure and behavior of the
 * plugin system. It allows for modular and dynamic integration of different components, such as
 * walls in the labyrinth, executable actions, initializers, and the core elements of a plugin.</p>
 *
 * <p>Key interfaces and classes include:</p>
 * <ul>
 *     <li>{@link net.regsirius06.engine.API.Loadable} - A marker interface indicating that an object is
 *     eligible for dynamic loading into the system.</li>
 *     <li>{@link net.regsirius06.engine.API.Core} - Represents the core plugin that defines the structure
 *     and loading mechanism for other plugin components.</li>
 *     <li>{@link net.regsirius06.engine.API.WallState} - Defines the properties and behaviors of walls in the
 *     labyrinth, such as whether the wall is light, empty, generable, or what color it is.</li>
 *     <li>{@link net.regsirius06.engine.API.Executable} - Represents executable actions within a plugin, with
 *     methods for defining the name and execution logic.</li>
 *     <li>{@link net.regsirius06.engine.API.Initializer} - A base class for providing ways to initialize objects
 *     of a specified type.</li>
 * </ul>
 *
 * <p>These classes and interfaces serve as the foundation for building modular components that can be 
 * dynamically loaded and integrated into the main application.</p>
 */
package net.regsirius06.engine.API;
