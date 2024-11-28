package net.regsirius06.engine.labyrinth;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

import static net.regsirius06.engine.labyrinth.Algorithm.*;

public class Labyrinth2D implements Iterable<List<WallType>> {
    private final int dimension;
    private final List<LightSource> lightSources;
    private final List<WallType> labyrinth;
    private final Point spawnPoint;

    public Labyrinth2D(int dimension, int quantityOfLights) {
        this.dimension = transformDimension(dimension);
        this.labyrinth = EllerGenLabyrinth(dimension);
        this.lightSources = genLightSources(this.labyrinth, quantityOfLights, transformDimension(dimension));
        this.spawnPoint = genSpawnPoint(this.labyrinth, this.lightSources, transformDimension(dimension));
    }

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

    private Point genSpawnPoint(List<WallType> labyrinth, @NotNull List<LightSource> lightSources, int dimension) {
        Random random = new Random();
        List<Point> emptyCells = getEmptyPoints(labyrinth, dimension);
        emptyCells.removeAll(lightSources.stream().map(LightSource::getPoint).toList());

        return emptyCells.get(random.nextInt(emptyCells.size()));
    }

    public WallType get(int x, int y) {
        return this.labyrinth.get(getIndex(x, y, dimension));
    }

    public boolean isCollidingWithWall(double x, double y) {
        int gridX = (int) x;
        int gridY = (int) y;

        if (gridX < 0 || gridX >= dimension || gridY < 0 || gridY >= dimension) {
            return true;
        }

        WallType wallType = this.get(gridX, gridY);
        return wallType.notEmpty();
    }

    public @Nullable LightSource getLightSource(double x, double y) {
        for (LightSource light: lightSources) {
            if (light.isPointInside(x, y)) {
                return light;
            }
        }
        return null;
    }

    @Contract(" -> new")
    public @NotNull Point getSpawnPoint() {
        return this.spawnPoint;
    }

    public int getDimension() {
        return dimension;
    }

    public List<LightSource> getLightSources() {
        return lightSources;
    }

    /**
     * Returns an iterator over elements of type {@code WallType}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<List<WallType>> iterator() {
        return new LabyrinthIterator(this);
    }

    private static class LabyrinthIterator implements Iterator<List<WallType>> {
        private final List<List<WallType>> list;
        private int index = 0;

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
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public List<WallType> next() {
            try {
                return list.get(index++);
            } catch (RuntimeException e) {
                throw new NoSuchElementException("The iterator has no more elements");
            }
        }
    }
}
