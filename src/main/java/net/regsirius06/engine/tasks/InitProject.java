package net.regsirius06.engine.tasks;

import net.regsirius06.engine.generator.CodeCollectorSystem;
import net.regsirius06.engine.generator.GenPluginProject;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for generating a plugin project template.
 * <p>
 * The {@link #main(String[])} method serves as the entry point for creating a new plugin project.
 * It takes two command-line arguments: the name of the plugin and its version.
 * For each build tool available in {@link CodeCollectorSystem}, it generates a project template
 * with the provided plugin name, version, and a default project structure.
 * </p>
 * <p>
 * The project template is generated in the "./projects/{buildTool}/" directory for each build tool
 * defined in {@link CodeCollectorSystem}.
 * </p>
 * @see GenPluginProject
 * @see CodeCollectorSystem
 */
public class InitProject {

    /**
     * The main method for generating a new plugin project template.
     * It requires two arguments: the plugin name and version. The method will create a project template
     * for each build tool specified in {@link CodeCollectorSystem}.
     *
     * @param args command-line arguments where:
     *             - args[0] is the plugin name,
     *             - args[1] is the plugin version.
     * @throws RuntimeException if the number of arguments is not exactly 2.
     */
    public static void main(String @NotNull [] args) throws RuntimeException {
        if (args.length != 2) {
            throw new RuntimeException("Must be provided 2 args, got: " + args.length);
        }
        String name = args[0];
        String version = args[1];
        for (CodeCollectorSystem buildTool: CodeCollectorSystem.values()) {
            GenPluginProject generator = new GenPluginProject(
                    "Example",
                    "com.example",
                    version + "-0.0.1",
                    buildTool,
                    "examples/",
                    name
            );
            generator.generateProject();
        }
    }
}
