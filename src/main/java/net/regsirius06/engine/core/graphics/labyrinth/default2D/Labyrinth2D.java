package net.regsirius06.engine.core.graphics.labyrinth.default2D;

import net.regsirius06.engine.utils.ILabyrinth2D;
import net.regsirius06.engine.point.Point;
import net.regsirius06.engine.point.Wall;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

import static net.regsirius06.engine.core.graphics.labyrinth.default2D.Algorithm.*;
import static net.regsirius06.engine.plugins.loaders.PluginsLoader.PLUGINS;

/**
 * Represents a 2D labyrinth consisting of walls and light sources.
 * <p>
 * This class provides functionality for generating and interacting with a labyrinth in a 2D grid.
 * It allows the management of walls, light sources, and spawn points, as well as collision detection.
 * </p>
 */
public class Labyrinth2D implements Iterable<List<Wall>>, ILabyrinth2D {

    private final int dimension;
    private final List<Wall> labyrinth;
    private final Point spawnPoint;
    private final long seed;

    /**
     * Constructs a new {@link Labyrinth2D} with the specified dimension and number of light sources.
     * <p>
     * This constructor generates a labyrinth and randomly places light sources and a spawn point.
     * </p>
     *
     * @param dimension the dimension of the labyrinth (width and height of a square grid)
     * @param quantityOfLights the number of light sources to generate in the labyrinth
     * @param seed the random seed for labyrinth generation
     */
    public Labyrinth2D(int dimension, int quantityOfLights, long seed) {
        this.dimension = transformDimension(dimension);
        this.labyrinth = EllerGenLabyrinth(dimension, seed);
        genLightSources(this, quantityOfLights, transformDimension(dimension));
        this.spawnPoint = genSpawnPoint(this.labyrinth, transformDimension(dimension));
        this.seed = seed;
    }

    /**
     * Generates a list of light sources in the labyrinth at random empty positions.
     * <p>
     * This method places the specified number of light sources randomly in empty cells within the labyrinth.
     * </p>
     *
     * @param labyrinth the labyrinth object where light sources will be placed
     * @param quantity the number of light sources to place
     * @param dimension the dimension of the labyrinth grid
     */
    private static void genLightSources(@NotNull Labyrinth2D labyrinth, int quantity, int dimension) {
        Random random = new Random();
        List<Point> emptyCells = getEmptyPoints(labyrinth.labyrinth, dimension);

        for (int i = 0; i < quantity; i++) {
            if (!emptyCells.isEmpty()) {
                Point randomCell = emptyCells.remove(random.nextInt(emptyCells.size()));
                labyrinth.set(randomCell.getPoint(), new Wall(randomCell, Objects.requireNonNull(
                        PLUGINS.getBase().getWallStates().getNewRandomWallState(
                                PLUGINS.getBase().getWallStates().getGenerableLights()
                        )
                )));
            }
        }
    }

    /**
     * Returns a list of empty cells (points) in the labyrinth where walls are not present.
     *
     * @param labyrinth the labyrinth layout as a list of walls
     * @param dimension the dimension of the labyrinth
     * @return a list of empty {@link Point} objects
     */
    private static @NotNull List<Point> getEmptyPoints(List<Wall> labyrinth, int dimension) {
        List<Point> emptyCells = new ArrayList<>();

        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (labyrinth.get(getIndex(x, y, dimension)).getState().isEmpty() &&
                        !labyrinth.get(getIndex(x, y, dimension)).getState().isLight()) {
                    emptyCells.add(new Point(x, y));
                }
            }
        }

        return emptyCells;
    }

    /**
     * Generates a random spawn point in the labyrinth that is not a light source location.
     * <p>
     * This method ensures that the spawn point is placed in an empty space, avoiding areas
     * occupied by walls or light sources.
     * </p>
     *
     * @param labyrinth the labyrinth layout
     * @param dimension the dimension of the labyrinth
     * @return a {@link Point} representing the randomly chosen spawn location
     */
    private static @NotNull Point genSpawnPoint(List<Wall> labyrinth, int dimension) {
        Random random = new Random();
        List<Point> emptyCells = getEmptyPoints(labyrinth, dimension);

        return emptyCells.get(random.nextInt(emptyCells.size())).movePoint(0.5, 0.5);
    }

    /**
     * Retrieves the wall at the specified coordinates (x, y) in the labyrinth.
     * <p>
     * The wall represents an element of the labyrinth at the given grid location.
     * </p>
     *
     * @param x the x-coordinate of the wall
     * @param y the y-coordinate of the wall
     * @return the {@link Wall} at the given coordinates
     */
    @Override
    public final Wall get(int x, int y) {
        return this.labyrinth.get(getIndex(x, y, dimension));
    }

    /**
     * Sets the wall at the specified coordinates (x, y) in the labyrinth.
     * <p>
     * This method allows you to modify the state of a specific wall in the labyrinth.
     * </p>
     *
     * @param x the x-coordinate of the wall
     * @param y the y-coordinate of the wall
     * @param wall the new wall type to set
     */
    @Override
    public final void set(int x, int y, Wall wall) {
        this.labyrinth.set(getIndex(x, y, dimension), wall);
    }

    /**
     * Checks whether the specified coordinates are within the bounds of the labyrinth.
     * <p>
     * This method ensures that any point being accessed lies within the valid range of the labyrinth's grid.
     * </p>
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the coordinates are within bounds, false otherwise
     */
    @Override
    public boolean checkAddress(int x, int y) {
        return !(x < 0 || x >= dimension || y < 0 || y >= dimension);
    }

    /**
     * Checks if the specified coordinates are colliding with a wall in the labyrinth.
     * <p>
     * This method detects if a point within the labyrinth's grid is occupied by a non-empty wall.
     * </p>
     *
     * @param x the x-coordinate of the point to check
     * @param y the y-coordinate of the point to check
     * @return true if the point is colliding with a wall, false otherwise
     */
    @Override
    public boolean isCollidingWithWall(double x, double y) {
        int gridX = (int) x;
        int gridY = (int) y;

        if (!this.checkAddress(gridX, gridY)) {
            return true;
        }

        Wall wall = this.get(gridX, gridY);
        return !wall.getState().isEmpty();
    }

    /**
     * Returns the spawn point of the labyrinth.
     * <p>
     * The spawn point is the location where the player or character starts within the labyrinth.
     * </p>
     *
     * @return the {@link Point} representing the spawn point
     */
    @Override
    public @NotNull Point getSpawnPoint() {
        return this.spawnPoint;
    }

    /**
     * Returns the dimension of the labyrinth (i.e., the width and height of the grid).
     * <p>
     * The dimension defines the size of the labyrinth's grid, both horizontally and vertically.
     * </p>
     *
     * @return the dimension of the labyrinth
     */
    @Override
    public int getDimension() {
        return dimension;
    }

    /**
     * Returns the random seed used for labyrinth generation.
     * <p>
     * This seed is used to ensure reproducibility of the labyrinth generation process.
     * </p>
     *
     * @return the seed used for labyrinth generation
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Returns an iterator over the rows of the labyrinth.
     * <p>
     * This iterator allows you to traverse the labyrinth row by row.
     * </p>
     *
     * @return an iterator for the labyrinth
     */
    @NotNull
    @Override
    public Iterator<List<Wall>> iterator() {
        return new LabyrinthIterator(this);
    }

    /**
     * An iterator implementation for the labyrinth.
     * <p>
     * This class allows iteration over the rows of the labyrinth.
     * </p>
     */
    private static class LabyrinthIterator implements Iterator<List<Wall>> {
        private final List<List<Wall>> list;
        private int index = 0;

        /**
         * Constructs a new iterator for the labyrinth.
         *
         * @param labyrinth the labyrinth to iterate over
         */
        public LabyrinthIterator(@NotNull Labyrinth2D labyrinth) {
            list = new ArrayList<>();
            for (int i = 0; i < labyrinth.dimension; i++) {
                list.add(new ArrayList<>());
                for (int j = 0; j < labyrinth.dimension; j++) {
                    list.get(i).add(labyrinth.get(i, j));
                }
            }
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public List<Wall> next() {
            return list.get(index++);
        }
    }
}
