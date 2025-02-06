package net.regsirius06.engine.plugins.loaders;

import net.regsirius06.engine.API.Core;
import net.regsirius06.engine.API.WallState;
import net.regsirius06.engine.core.walls.End;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A {@link PluginLoader} implementation for loading {@link WallState} plugins.
 * This class extends {@link DefaultPluginLoader} to load plugins of type {@link WallState}
 * and categorizes them into different types based on their generability, emptiness, and light status.
 *
 * <p>The loader provides methods to retrieve generable wall states, lights, and empty states
 * separately, and also allows for selecting a random wall state or creating a new random instance
 * of a {@link WallState} plugin.</p>
 */
public final class WallStatesPluginLoader extends DefaultPluginLoader<WallState> {

    private List<WallState> generableWalls;
    private List<WallState> generableEmpties;
    private List<WallState> generableLights;
    private final Random random = new Random();

    /**
     * Constructs a new {@code WallStatesPluginLoader} for the given core type.
     *
     * <p>This constructor initializes the loader and automatically loads the wall state plugins
     * using the {@link DefaultPluginLoader#loadPlugins()} method.</p>
     *
     * @param type the core type to associate with the loader
     */
    public WallStatesPluginLoader(@NotNull Class<? extends Core> type) {
        super(WallState.class, type);
    }

    /**
     * Loads all available {@link WallState} plugins and categorizes them into three groups:
     * generable walls, generable lights, and generable empties.
     *
     * <p>Generable walls are walls that are not empty and are not of type {@link End}.
     * Generable lights are walls that have a light state, and generable empties are walls
     * that are empty.</p>
     */
    @Override
    public void loadPlugins() {
        super.loadPlugins();
        generableWalls = new ArrayList<>();
        generableEmpties = new ArrayList<>();
        generableLights = new ArrayList<>();

        for (WallState state : this.getPlugins()) {
            if (state.isGenerable()) {
                if (state.isLight()) {
                    generableLights.add(state);
                } else if (!state.isEmpty() && !state.thisEquals(End.class)) {
                    generableWalls.add(state);
                } else if (state.isEmpty()) {
                    generableEmpties.add(state);
                }
            }
        }
    }

    /**
     * Returns the list of generable wall states.
     *
     * <p>Generable walls are walls that are not empty and are not of type {@link End}.</p>
     *
     * @return a list of generable wall states
     */
    public List<? extends WallState> getGenerableWalls() {
        return generableWalls;
    }

    /**
     * Returns the list of generable light wall states.
     *
     * @return a list of generable light wall states
     */
    public List<? extends WallState> getGenerableLights() {
        return generableLights;
    }

    /**
     * Returns the list of generable empty wall states.
     *
     * @return a list of generable empty wall states
     */
    public List<? extends WallState> getGenerableEmpties() {
        return generableEmpties;
    }

    /**
     * Returns a random {@link WallState} from the provided list.
     *
     * @param list the list from which a random wall state will be selected
     * @return a randomly selected wall state
     */
    public WallState getRandomWallStateInstance(@NotNull List<? extends WallState> list) {
        return list.get(random.nextInt(0, list.size()));
    }

    /**
     * Creates and returns a new instance of a random {@link WallState} from the provided list.
     *
     * <p>This method uses reflection to instantiate a new object of the selected {@link WallState}
     * type from the list.</p>
     *
     * @param list the list from which a new random wall state will be instantiated
     * @return a new instance of a randomly selected wall state
     * @throws RuntimeException if there is an error instantiating the wall state
     */
    public @NotNull WallState getNewRandomWallState(@NotNull List<? extends WallState> list) throws RuntimeException {
        try {
            return list.get(random.nextInt(0, list.size())).getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
