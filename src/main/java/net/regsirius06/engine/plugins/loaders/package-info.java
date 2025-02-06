/**
 * This package contains classes responsible for loading and managing plugins
 * within the engine, specifically for loading {@link net.regsirius06.engine.API.Loadable}
 * plugins such as {@link net.regsirius06.engine.API.Core}, {@link net.regsirius06.engine.API.Executable},
 * {@link net.regsirius06.engine.API.WallState} plugins, and initializers for labyrinth panels
 * ({@link net.regsirius06.engine.utils.panels.AbstractLabyrinthPanel}).
 *
 * <p>The loaders in this package leverage the {@link java.util.ServiceLoader} mechanism to
 * dynamically load plugins and provide access to them. Each loader is responsible for
 * filtering, validating, and providing access to a specific category of plugins,
 * whether it's executable actions, wall states, or labyrinth panel initializers.</p>
 *
 * <p>Key classes in this package include:</p>
 * <ul>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.PluginLoader} - Interface defining the structure for all plugin loaders.</li>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.DefaultPluginLoader} - Default implementation of the {@link net.regsirius06.engine.plugins.loaders.PluginLoader} interface.</li>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.ExecutablesPluginLoader} - Loader for {@link net.regsirius06.engine.API.Executable} plugins.</li>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.WallStatesPluginLoader} - Loader for {@link net.regsirius06.engine.API.WallState} plugins.</li>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.LabyrinthPanelsPluginLoader} - Loader for {@link net.regsirius06.engine.utils.panels.AbstractLabyrinthPanel} initializers.</li>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.PluginsLoader} - Concrete loader for {@link net.regsirius06.engine.API.Core} plugins, responsible for loading the core plugins.</li>
 *     <li>{@link net.regsirius06.engine.plugins.loaders.InitializersPluginLoader} - Loader for {@link net.regsirius06.engine.API.Initializer} plugins, specifically for initializing various objects like labyrinth panels.</li>
 * </ul>
 */
package net.regsirius06.engine.plugins.loaders;
