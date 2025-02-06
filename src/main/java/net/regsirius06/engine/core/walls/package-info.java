/**
 * Provides implementations of {@link net.regsirius06.engine.API.WallState} representing different types of walls
 * within the labyrinth system.
 *
 * <p>This package defines various wall states, including:</p>
 * <ul>
 *     <li>{@link net.regsirius06.engine.core.walls.Default} - A standard wall with a defined absorption coefficient.</li>
 *     <li>{@link net.regsirius06.engine.core.walls.Empty} - An empty space, representing the absence of a wall.</li>
 *     <li>{@link net.regsirius06.engine.core.walls.End} - A boundary wall marking the limits of the world.</li>
 *     <li>{@link net.regsirius06.engine.core.walls.Light} - A special type of wall that acts as a light source.</li>
 * </ul>
 *
 * <p>Each implementation provides specific behavior regarding whether the wall is solid, transparent, or generable,
 * as well as defining its color and absorption properties.</p>
 *
 * @see net.regsirius06.engine.API.WallState
 */
package net.regsirius06.engine.core.walls;
