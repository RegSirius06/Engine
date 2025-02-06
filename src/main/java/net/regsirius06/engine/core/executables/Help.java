package net.regsirius06.engine.core.executables;

import net.regsirius06.engine.API.Executable;
import net.regsirius06.engine.API.annotations.ImplementationOf;
import net.regsirius06.engine.utils.help.InfoExecutable;

import java.util.List;

/**
 * Provides information about the core system of the game engine.
 * <p>
 * This class extends {@link InfoExecutable} and serves as an {@code "About"}
 * dialog for the core of the game. It provides details such as authors,
 * version, description, dependencies, and contribution information.
 * </p>
 * <p>
 * The {@link Help} executable is automatically included in the system and
 * ensures that users can always access relevant information about the
 * game engine's core functionalities.
 * </p>
 *
 * @see InfoExecutable
 * @see Executable
 */
@ImplementationOf(Executable.class)
public class Help extends InfoExecutable {
    /**
     * Returns a list of authors who contributed to the core system.
     *
     * @return a list containing the main author "Sirius06"
     */
    @Override
    protected List<String> authors() {
        return List.of("Sirius06");
    }

    /**
     * Provides a description of the core system of the engine.
     *
     * @return a string describing the purpose and state of the core system
     */
    @Override
    protected String description() {
        return """
                This is the core of the project from the developer of this game.
                It is necessary for stable operation of the system.
                The default labyrinth is implemented here.
                The project is developing, your plugins can become part of the core.
                Join in!
                
                P.S. Sorry if I forgot to change version
                
                :-)""";
    }

    /**
     * Returns the version of the core system.
     *
     * @return the version string "beta-1.0.0"
     */
    @Override
    protected String version() {
        return "beta-1.0.0";
    }

    /**
     * Provides a list of dependencies required by the core system.
     *
     * @return a list of dependencies, including Java 17 and various libraries
     */
    @Override
    protected List<String> dependencies() {
        return List.of(
                "Java 17",
                "org.jetbrains:annotations:24.0.0",
                "org.apache.commons:commons-compress:1.21",
                "org.reflections:reflections:0.10.2",
                "ch.qos.logback:logback-classic:1.4.11"
        );
    }

    /**
     * Returns the URL for contributing to the core system.
     *
     * @return the GitHub repository link for contributions
     */
    @Override
    protected String contribute() {
        return "https://github.com/RegSirius06/Engine/";
    }
}
