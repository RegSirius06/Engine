/**
 * Provides annotations for use in the plugin system.
 *
 * <p>The annotations in this package are used to define key aspects of plugins, such as identifying
 * mod cores, ensuring their uniqueness, establishing relationships between plugins, and integrating
 * functionality across plugins or with the core system.</p>
 *
 * <p>Annotations like {@link net.regsirius06.engine.API.annotations.ModId},
 * {@link net.regsirius06.engine.API.annotations.ImplementationOf}, and
 * {@link net.regsirius06.engine.API.annotations.AddTo} enable defining
 * dependencies between plugins, automatically generating necessary files, and integrating plugin
 * functionality either within other plugins or directly into the core system.</p>
 *
 * @see net.regsirius06.engine.API.Core
 * @see net.regsirius06.engine.API.Loadable
 */
package net.regsirius06.engine.API.annotations;
