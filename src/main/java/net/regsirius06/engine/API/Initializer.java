package net.regsirius06.engine.API;

import net.regsirius06.engine.core.initializers.LabyrinthPanelInit;
import net.regsirius06.engine.core.panels.LabyrinthPanel;
import net.regsirius06.engine.plugins.loaders.InitializersPluginLoader;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Abstract class that provides a way to initialize objects of type {@code T}.
 *
 * <p>{@code Initializer} is used to supply a method of creating an object of type {@code T}.
 * The class does not load anything directly, but custom initializers can be loaded via
 * {@link InitializersPluginLoader} if needed. Subclasses should implement the {@link #getName()}
 * and {@link #create(Object...)} methods to define the specific initialization logic.</p>
 *
 * <p>In the application, an initializer is typically used in the context of creating
 * objects like {@link LabyrinthPanel}, as shown in {@link LabyrinthPanelInit},
 * where the initializer provides a way to create specific instances of complex objects.</p>
 *
 * <p>The generic type {@code T} represents the type of object that will be created, and the
 * {@link #getType()} method returns the class of that type.</p>
 *
 * @param <T> the type of object that this initializer will create
 *
 * @see Loadable
 * @see InitializersPluginLoader
 * @see net.regsirius06.engine.plugins.loaders.LabyrinthPanelsPluginLoader
 * @see net.regsirius06.engine.utils.panels.AbstractLabyrinthPanel
 * @see InitializersPluginLoader
 */
public abstract class Initializer<T> implements Loadable, Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * The {@code Class} object representing the generic type {@code T} of this initializer.
     * <p>
     * This field stores the actual class of the type {@code T}, which is determined at runtime
     * through reflection in the constructor. It allows access to the type of object that this
     * initializer is responsible for creating.
     * <p>
     * Subclasses of {@code Initializer<T>} must specify a concrete type for {@code T},
     * otherwise, a {@link RuntimeException} will be thrown during instantiation.
     *
     * @see #Initializer()
     * @see #getType()
     */
    private final Class<T> type;

    /**
     * Constructs a new {@code Initializer} and extracts the generic type {@code T} from the subclass.
     *
     * <p>This constructor uses reflection to determine the actual type of {@code T} based on the subclass
     * that extends {@code Initializer}. If the type parameter {@code T} is not specified, a runtime exception
     * will be thrown.</p>
     *
     * @throws RuntimeException if the type parameter {@code T} is not specified in the subclass
     */
    @SuppressWarnings(value = "unchecked")
    public Initializer() {
        Type superClass = getClass().getGenericSuperclass();

        if (superClass instanceof ParameterizedType) {
            this.type = (Class<T>) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        } else {
            throw new RuntimeException("Type parameter T is not specified.");
        }
    }

    /**
     * Returns the {@code Class} object of the type {@code T}.
     *
     * <p>This method provides access to the type of object that will be created by this initializer.</p>
     *
     * @return the {@code Class} object representing the type of {@code T}
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Returns the name of the initializer.
     *
     * <p>This method must be implemented by subclasses to provide a human-readable name for the initializer.
     * This name is typically used to identify the initializer in the application.</p>
     *
     * @return the name of the initializer
     */
    public abstract String getName();

    /**
     * Creates an instance of type {@code T} using the provided arguments.
     *
     * <p>This method must be implemented by subclasses to define how an object of type {@code T} is created.
     * The {@code objects} parameter can contain any necessary data required for object creation, and the method
     * should return the created instance. It may throw exceptions if the arguments are invalid or if the creation
     * fails for any reason.</p>
     *
     * @param objects the arguments needed to create an object of type {@code T}
     * @return an instance of type {@code T}
     * @throws IllegalArgumentException if the arguments are invalid or insufficient for object creation
     */
    public abstract T create(Object... objects) throws IllegalArgumentException;
}
