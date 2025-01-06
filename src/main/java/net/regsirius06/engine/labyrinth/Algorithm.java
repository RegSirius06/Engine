package net.regsirius06.engine.labyrinth;

import net.regsirius06.engine.point.Point;
import net.regsirius06.engine.point.WallEnum;
import net.regsirius06.engine.point.WallType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

/**
 * This class provides a set of algorithms for generating labyrinths.
 * It contains methods for transforming grid dimensions, calculating
 * indices, and generating labyrinths using different algorithms such as
 * Sirius' and Eller's.
 * <p>
 * The algorithms create labyrinths as a list of {@link WallType} objects,
 * representing walls, empty spaces, and boundaries. It also includes
 * helper methods for transforming 2D grid coordinates into 1D indices and vice versa.
 * </p>
 */
public final class Algorithm {

    // Private constructor to prevent instantiation
    private Algorithm() {
    }

    /**
     * Transforms the input dimension to account for the walls of the labyrinth.
     * The labyrinth grid is represented as a square, where each dimension is
     * increased by 1 to include walls around the maze.
     *
     * @param n the size of the labyrinth without walls (number of cells in one row/column)
     * @return the transformed dimension, which is equal to 2 * n + 1
     */
    public static int transformDimension(int n) {
        return 2 * n + 1;
    }

    /**
     * Converts 2D grid coordinates (x, y) to a 1D index based on the given dimension.
     * This is useful for accessing elements in a list representation of the labyrinth.
     *
     * @param x        the row coordinate
     * @param y        the column coordinate
     * @param dimension the total dimension of the grid (including walls)
     * @return the 1D index corresponding to the 2D coordinates
     */
    public static int getIndex(int x, int y, int dimension) {
        return x * dimension + y;
    }

    /**
     * Converts a 1D index into 2D coordinates based on the given dimension.
     * This is the reverse operation of {@link #getIndex(int, int, int)}.
     *
     * @param index     the 1D index
     * @param dimension the total dimension of the grid (including walls)
     * @return a {@link Point} object representing the 2D coordinates of the index
     */
    @Contract("_, _ -> new")
    public static @NotNull Point getPoint(int index, int dimension) {
        return new Point(index / dimension, index % dimension);
    }

    /**
     * Generates a labyrinth using the Sirius's algorithm.
     * The labyrinth is represented as a list of {@link WallType} objects, where
     * each wall can be either a wall, an empty space, or the labyrinth's boundary.
     * The algorithm randomly places walls and open spaces within the grid.
     *
     * @param n the size of the labyrinth (number of cells in one row/column)
     * @return a list of {@link WallType} objects representing the generated labyrinth
     */
    public static @NotNull List<WallType> SiriusGenLabyrinth(int n) {
        int dimension = transformDimension(n); // with walls; unnecessary
        List<WallType> list = new ArrayList<>();
        Random rand = new Random();

        // Fill the labyrinth grid with walls and empty spaces
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == 0 || i == dimension - 1 || j == 0 || j == dimension - 1) {
                    list.add(new WallType(i, j, WallEnum.END)); // Boundary walls
                    continue;
                }
                // Randomly assign wall or empty space
                list.add(rand.nextBoolean() ?
                        new WallType(i, j, WallEnum.DEFAULT) :
                        new WallType(i, j, WallEnum.EMPTY));
            }
        }
        return list;
    }

    /**
     * Generates a labyrinth using the Eller's algorithm.
     * This is a more complex maze generation algorithm that uses a union-find
     * data structure to progressively connect sets of cells in the maze grid.
     *
     * @param n the size of the labyrinth (number of cells in one row/column)
     * @return a list of {@link WallType} objects representing the generated labyrinth
     */
    public static @NotNull List<WallType> EllerGenLabyrinth(int n) {
        int dimension = transformDimension(n); // With walls; necessary
        List<WallType> list = new ArrayList<>();
        IntStream.range(0, dimension * dimension).forEach(e ->
                list.add(new WallType(getPoint(e, dimension), WallEnum.DEFAULT)));

        // Set the boundary walls
        for (int i = 0; i < dimension; i++) {
            list.set(getIndex(i, 0, dimension), new WallType(i, 0, WallEnum.END));
            list.set(getIndex(0, i, dimension), new WallType(0, i, WallEnum.END));
            list.set(getIndex(i, dimension - 1, dimension), new WallType(i, dimension - 1, WallEnum.END));
            list.set(getIndex(dimension - 1, i, dimension), new WallType(dimension - 1, i, WallEnum.END));
        }

        // Initialize union-find data structure
        int[] row_stack = new int[dimension];
        List<int[]> set_list = new ArrayList<>();
        int set_index = 1;
        Random random = new Random();

        // Generate the labyrinth by connecting cells row by row
        for (int x = 1; x < dimension - 1; x += 2) {
            Deque<Boolean> connect_list = new LinkedList<>();
            if (row_stack[0] == 0) {
                row_stack[0] = set_index;
                set_index++;
            }

            // Randomly determine whether to connect cells horizontally or not
            for (int y = 1; y < n; y++) {
                if (random.nextBoolean()) {
                    if (row_stack[y] != 0) {
                        int old_index = row_stack[y];
                        int new_index = row_stack[y - 1];
                        if (old_index != new_index) {
                            for (int i = 0; i < row_stack.length; i++) {
                                if (row_stack[i] == old_index) {
                                    row_stack[i] = new_index;
                                }
                            }
                        }
                    } else {
                        row_stack[y] = row_stack[y - 1];
                    }
                    connect_list.add(true);
                } else {
                    if (row_stack[y] == 0) {
                        row_stack[y] = set_index;
                        set_index++;
                    }
                    connect_list.add(false);
                }
            }

            // Connect cells and mark visited
            for (int y = 0; y < n; y++) {
                int maze_col = 2 * y + 1;
                set_list.add(new int[]{row_stack[y], maze_col});
                list.set(getIndex(x, maze_col, dimension),
                        new WallType(x, maze_col, WallEnum.EMPTY)); // Mark visited
                if (y < dimension - 1 && Boolean.TRUE.equals(connect_list.pollFirst())) {
                    list.set(getIndex(x, maze_col + 1, dimension),
                            new WallType(x, maze_col + 1, WallEnum.EMPTY)); // Mark visited
                }
            }

            // Handle last row connections and reset the row stack
            if (x == dimension - 2) {
                for (int y = 1; y < n; y++) {
                    int new_index = row_stack[y - 1];
                    int old_index = row_stack[y];
                    if (new_index != old_index) {
                        for (int i = 0; i < row_stack.length; i++) {
                            if (row_stack[i] == old_index) {
                                row_stack[i] = new_index;
                            }
                        }
                        list.set(getIndex(x, 2 * y, dimension),
                                new WallType(x, 2 * y, WallEnum.EMPTY)); // Mark visited
                    }
                }
            }

            // Reset the row stack and sort the set list
            Arrays.fill(row_stack, 0);
            set_list.sort((a, b) -> Integer.compare(b[0], a[0]));

            // Process sets to link them together vertically
            while (!set_list.isEmpty()) {
                Deque<int[]> sub_set_list = new LinkedList<>();
                int sub_set_index = set_list.get(set_list.size() - 1)[0];
                while (!set_list.isEmpty() && set_list.get(set_list.size() - 1)[0] == sub_set_index) {
                    sub_set_list.add(set_list.remove(set_list.size() - 1));
                }
                boolean linked = false;
                while (!linked) {
                    for (int[] sub_set_item : sub_set_list) {
                        if (random.nextBoolean()) {
                            linked = true;
                            int link_set = sub_set_item[0];
                            int link_position = sub_set_item[1];
                            row_stack[link_position / 2] = link_set;
                            if (x + 1 != dimension - 1) {
                                list.set(getIndex(x + 1, link_position, dimension),
                                        new WallType(x + 1, link_position, WallEnum.EMPTY)); // Mark visited
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
