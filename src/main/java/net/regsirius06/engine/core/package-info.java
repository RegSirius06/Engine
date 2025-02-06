/**
 * Defines the core functionalities of the game engine, providing the essential foundation for game logic,
 * plugin integration, and core system components.
 *
 * <p>This package contains essential classes and interfaces that establish the foundation of the engine's functionality,
 * and serve as the entry points for plugin-based extensibility.</p>
 *
 * <p>The core package serves as the main entry point of the engine, implementing the {@link net.regsirius06.engine.API.Core} interface.
 * It manages key plugin loaders that facilitate the extension and customization of the engine, including:</p>
 * <ul>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.WallStatesPluginLoader} - Handles custom wall state definitions.</li>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.ExecutablesPluginLoader} - Manages executable commands.</li>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.LabyrinthPanelsPluginLoader} - Supports labyrinth panel customization.</li>
 * </ul>
 *
 * <p>Key modules in this package include:</p>
 * <ul>
 *   <li>{@link net.regsirius06.engine.core.walls} - Contains various implementations of {@link net.regsirius06.engine.API.WallState},
 *       representing different types of walls within the labyrinth system, such as standard walls, light walls, and empty spaces.</li>
 *   <li>{@link net.regsirius06.engine.core.executables} - Implements commands via the {@link net.regsirius06.engine.API.Executable} interface,
 *       including the core game engine commands like {@link net.regsirius06.engine.core.executables.Help}, which provide important
 *       information and assistance to the user.</li>
 *   <li>{@link net.regsirius06.engine.core.graphics} - Contains utilities and classes for graphical operations, such as color manipulation,
 *       raycasting, labyrinth rendering, and minimap generation. This package is essential for rendering the 2D labyrinth environment.</li>
 *   <li>{@link net.regsirius06.engine.core.initializers} - Defines the initialization logic for various components and plugins,
 *       ensuring proper configuration and setup during the engine startup.</li>
 *   <li>{@link net.regsirius06.engine.core.panels} - Provides implementations for the graphical panels used in the game engine,
 *       such as the main labyrinth panel and auxiliary interface components.</li>
 * </ul>
 *
 * <p>The main class in this package is {@link net.regsirius06.engine.core.Base}, which acts as the fundamental core instance,
 * ensuring that all necessary plugin components are correctly initialized and accessible throughout the engine.</p>
 *
 * <p>Additionally, this package provides the necessary annotations, including {@link net.regsirius06.engine.API.annotations.ModId}
 * and {@link net.regsirius06.engine.API.annotations.ImplementationOf}, which facilitate plugin identification and integration.</p>
 *
 * @see net.regsirius06.engine.API.Core
 * @see net.regsirius06.engine.plugins.loaders.WallStatesPluginLoader
 * @see net.regsirius06.engine.plugins.loaders.ExecutablesPluginLoader
 * @see net.regsirius06.engine.plugins.loaders.LabyrinthPanelsPluginLoader
 * @see net.regsirius06.engine.API.annotations.ModId
 * @see net.regsirius06.engine.API.annotations.ImplementationOf
 */
package net.regsirius06.engine.core;
