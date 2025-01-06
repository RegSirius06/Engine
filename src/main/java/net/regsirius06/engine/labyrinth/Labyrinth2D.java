package net.regsirius06.engine.labyrinth;

import net.regsirius06.engine.point.LightSource;
import net.regsirius06.engine.point.Point;
import net.regsirius06.engine.point.WallEnum;
import net.regsirius06.engine.point.WallType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

import static net.regsirius06.engine.labyrinth.Algorithm.*;

/**
 * Represents a 2D labyrinth with walls and light sources.
 * This class provides functionality for managing and interacting with the labyrinth,
 * including generation, access to wall types, collision detection, and managing light sources.
 */
public class Labyrinth2D implements Iterable<List<WallType>> {
    private final int dimension;
    private final List<LightSource> lightSources;
    private final List<WallType> labyrinth;
    private final Point spawnPoint;

    /**
     * Constructs a new {@link Labyrinth2D} with the specified dimension and quantity of light sources.
     *
     * @param dimension the dimension of the labyrinth (width and height of a square grid)
     * @param quantityOfLights the number of light sources to generate in the labyrinth
     */
    public Labyrinth2D(int dimension, int quantityOfLights) {
        this.dimension = transformDimension(dimension);
        this.labyrinth = EllerGenLabyrinth(dimension);
        this.lightSources = genLightSources(this.labyrinth, quantityOfLights, transformDimension(dimension));
        for (LightSource light: lightSources) {
            this.set((int) light.getX(), (int) light.getY(), new WallType(light.getPoint(), WallEnum.LIGHT));
        }
        this.spawnPoint = genSpawnPoint(this.labyrinth, this.lightSources, transformDimension(dimension));
    }

    /**
     * Generates a list of light sources based on the given labyrinth and quantity.
     *
     * @param labyrinth the list representing the labyrinth layout
     * @param quantity the number of light sources to generate
     * @param dimension the dimension of the labyrinth
     * @return a list of generated {@link LightSource} objects
     */
    private static @NotNull List<LightSource> genLightSources(List<WallType> labyrinth, int quantity, int dimension) {
        Random random = new Random();
        List<Point> emptyCells = getEmptyPoints(labyrinth, dimension);
        List<LightSource> lightSources = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            if (!emptyCells.isEmpty()) {
                Point randomCell = emptyCells.remove(random.nextInt(emptyCells.size()));
                Color randomColor = LightSource.COLORS.get(random.nextInt(LightSource.COLORS.size()));
                lightSources.add(new LightSource(randomCell, randomColor));
            }
        }

        return lightSources;
    }

    /**
     * Returns a list of empty points in the labyrinth, where walls are not present.
     *
     * @param labyrinth the list representing the labyrinth layout
     * @param dimension the dimension of the labyrinth
     * @return a list of empty {@link Point} objects
     */
    private static @NotNull List<Point> getEmptyPoints(List<WallType> labyrinth, int dimension) {
        List<Point> emptyCells = new ArrayList<>();

        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (!labyrinth.get(getIndex(x, y, dimension)).notEmpty()) {
                    emptyCells.add(new Point(x, y));
                }
            }
        }

        return emptyCells;
    }

    /**
     * Generates a random spawn point inside the labyrinth, ensuring it is not a light source location.
     *
     * @param labyrinth the list representing the labyrinth layout
     * @param lightSources the list of light sources in the labyrinth
     * @param dimension the dimension of the labyrinth
     * @return a randomly generated {@link Point} for the spawn location
     */
    private @NotNull Point genSpawnPoint(List<WallType> labyrinth, @NotNull List<LightSource> lightSources, int dimension) {
        Random random = new Random();
        List<Point> emptyCells = getEmptyPoints(labyrinth, dimension);
        emptyCells.removeAll(lightSources.stream().map(LightSource::getPoint).toList());

        return emptyCells.get(random.nextInt(emptyCells.size())).movePoint(0.5, 0.5);
    }

    /**
     * Returns the wall type at the specified coordinates (x, y) in the labyrinth.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the {@link WallType} at the given coordinates
     */
    public WallType get(int x, int y) {
        return this.labyrinth.get(getIndex(x, y, dimension));
    }

    /**
     * Sets the wall type at the specified coordinates (x, y) in the labyrinth.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param wall the wall type to set
     */
    private void set(int x, int y, WallType wall) {
        this.labyrinth.set(getIndex(x, y, dimension), wall);
    }

    /**
     * Returns the wall type at the location of a given {@link Point}.
     *
     * @param p the point to get the wall type for
     * @param <P> the type of point (subtype of {@link Point})
     * @return the {@link WallType} at the given point
     */
    public <P extends Point> WallType get(@NotNull P p) {
        return this.get((int) p.getX(), (int) p.getY());
    }

    /**
     * Checks if the specified coordinates are within the bounds of the labyrinth.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are within bounds, false otherwise
     */
    public boolean checkAddress(int x, int y) {
        return !(x < 0 || x >= dimension || y < 0 || y >= dimension);
    }

    /**
     * Checks if the specified position (x, y) is colliding with a wall in the labyrinth.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if there is a collision with a wall, false otherwise
     */
    public boolean isCollidingWithWall(double x, double y) {
        int gridX = (int) x;
        int gridY = (int) y;

        if (!this.checkAddress(gridX, gridY)) {
            return true;
        }

        WallType wallType = this.get(gridX, gridY);
        return wallType.notEmpty();
    }

    /**
     * Returns the light source that occupies the position (x, y), if any.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the {@link LightSource} at the given position, or null if there is none
     */
    public @Nullable LightSource getLightSource(double x, double y) {
        for (LightSource light: lightSources) {
            if (light.isPointInside(x, y)) {
                return light;
            }
        }
        return null;
    }

    /**
     * Returns the spawn point of the labyrinth.
     *
     * @return the {@link Point} representing the spawn point
     */
    @Contract(" -> new")
    public @NotNull Point getSpawnPoint() {
        return this.spawnPoint;
    }

    /**
     * Returns the dimension of the labyrinth (i.e., the width/height of the square grid).
     *
     * @return the dimension of the labyrinth
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Returns a list of light sources present in the labyrinth.
     *
     * @return the list of {@link LightSource} objects
     */
    public List<LightSource> getLightSources() {
        return lightSources;
    }

    /**
     * Returns an iterator over the labyrinth, represented as a list of {@link WallType} objects.
     * Each list represents a row of the labyrinth.
     *
     * @return an iterator over the labyrinth
     */
    @NotNull
    @Override
    public Iterator<List<WallType>> iterator() {
        return new LabyrinthIterator(this);
    }

    /**
     * An iterator implementation for the labyrinth, allowing iteration over the rows of the labyrinth.
     */
    private static class LabyrinthIterator implements Iterator<List<WallType>> {
        private final List<List<WallType>> list;
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

        /**
         * Returns {@code true} if the iteration has more elements.
         *
         * @return {@code true} if there are more rows to iterate over
         */
        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        /**
         * Returns the next element in the iteration (i.e., the next row of the labyrinth).
         *
         * @return the next row in the labyrinth
         * @throws NoSuchElementException if there are no more elements to return
         */
        @Override
        public List<WallType> next() {
            try {
                return list.get(index++);
            } catch (RuntimeException e) {
                throw new NoSuchElementException("The iterator has no more elements.");
            }
        }
    }
}
