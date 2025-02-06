/**
 * Provides classes for managing plugins within the engine.
 * <p>
 * This package contains functionality for loading and managing plugins dynamically from
 * external JAR files in the {@code plugins} directory. It also includes classes for 
 * generating and managing metadata files for plugin initialization and implementation.
 * </p>
 * <p>
 * The main components of this package include:
 * <ul>
 *   <li>{@link net.regsirius06.engine.plugins.files.PluginDataProvider} - Responsible for generating metadata files for plugins annotated with {@link net.regsirius06.engine.API.annotations.ImplementationOf}.</li>
 *   <li>{@link net.regsirius06.engine.plugins.files.PluginManager} - Handles loading plugins from the {@code plugins} directory into the class loader.</li>
 * </ul>
 * <p>
 * The package facilitates plugin-based architecture in the engine, enabling dynamic loading
 * of classes that implement specific interfaces, and helps with organizing plugin-related
 * resources.
 * </p>
 */
package net.regsirius06.engine.plugins.files;
