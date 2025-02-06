/**
 * This package contains classes representing geometric points and related entities used in various
 * computational tasks, such as maze generation and pathfinding. The main classes are:
 * <ul>
 *     <li>{@link net.regsirius06.engine.point.Point} - represents a point in a 2D space with {@code x} and {@code y} coordinates.</li>
 *     <li>{@link net.regsirius06.engine.point.ColoredPoint} - extends {@code Point} and adds a color attribute to represent visual properties.</li>
 *     <li>{@link net.regsirius06.engine.point.Wall} - extends {@code ColoredPoint} and adds a state attribute, commonly used to represent walls in a maze.</li>
 * </ul>
 * These classes are particularly useful for applications dealing with grid-based environments, such as maze solvers
 * or pathfinding algorithms, where each point may represent either a free space or an obstacle (wall).
 */
package net.regsirius06.engine.point;
