package net.regsirius06.engine.labyrinth;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Algorithm {
    private Algorithm() {
    }

    public static int transformDimension(int n) {
        return 2 * n + 1;
    }

    public static int getIndex(int x, int y, int dimension) {
        return x * dimension + y;
    }

    public static @NotNull List<WallType> SiriusGenLabyrinth(int n) {
        int dimension = transformDimension(n); // with walls; unnecessary
        List<WallType> list = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == 0 || i == dimension - 1 || j == 0 || j == dimension - 1) {
                    list.add(WallType.END);
                    continue;
                }
                if (i == dimension / 2 && j == dimension / 2) {
                    list.add(WallType.EMPTY);
                    continue;
                }
                list.add((new Random().nextBoolean() ? WallType.DEFAULT : WallType.EMPTY));
            }
        }
        return list;
    }

    public static @NotNull List<WallType> EllerGenLabyrinth(int n) {
        int dimension = transformDimension(n); // With walls; necessary
        List<WallType> list = new ArrayList<>(Collections.nCopies(dimension * dimension, WallType.DEFAULT));
        for (int i = 0; i < dimension; i++) {
            list.set(getIndex(i, 0, dimension), WallType.END);
            list.set(getIndex(0, i, dimension), WallType.END);
            list.set(getIndex(i, dimension - 1, dimension), WallType.END);
            list.set(getIndex(dimension - 1, i, dimension), WallType.END);
        }

        int[] row_stack = new int[dimension];
        List<int[]> set_list = new ArrayList<>();
        int set_index = 1;
        Random random = new Random();

        for (int x = 1; x < dimension - 1; x += 2) {
            Deque<Boolean> connect_list = new LinkedList<>();
            if (row_stack[0] == 0) {
                row_stack[0] = set_index;
                set_index++;
            }
            for (int y = 1; y < n; y++) {
                if (random.nextBoolean()) { // unite with previous
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

            for (int y = 0; y < n; y++) {
                int maze_col = 2 * y + 1;
                set_list.add(new int[]{row_stack[y], maze_col});
                list.set(getIndex(x, maze_col, dimension), WallType.EMPTY); // make visited
                if (y < dimension - 1) {
                    if (Boolean.TRUE.equals(connect_list.pollFirst())) {
                        list.set(getIndex(x, maze_col + 1, dimension), WallType.EMPTY); // make visited
                    }
                }
            }

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
                        list.set(getIndex(x, 2 * y, dimension), WallType.EMPTY); // make visited
                    }
                }
            }

            Arrays.fill(row_stack, 0);

            set_list.sort((a, b) -> Integer.compare(b[0], a[0]));

            while (!set_list.isEmpty()) {
                Deque<int[]> sub_set_list = new LinkedList<>();
                int sub_set_index = set_list.get(set_list.size() - 1)[0];
                while (!set_list.isEmpty() && set_list.get(set_list.size() - 1)[0] == sub_set_index) {
                    sub_set_list.add(set_list.remove(set_list.size() - 1));
                }
                boolean linked = false;
                while (!linked) {
                    for (int[] sub_set_item : sub_set_list) {
                        if (random.nextBoolean()) { // net creation
                            linked = true;
                            int link_set = sub_set_item[0];
                            int link_position = sub_set_item[1];
                            row_stack[link_position / 2] = link_set; // new net
                            if (x + 1 != dimension - 1) {
                                list.set(getIndex(x + 1, link_position, dimension), WallType.EMPTY); // visited net
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
