package net.regsirius06.engine.generator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * An enum that represents different build systems (e.g., Gradle, Gradle Kotlin DSL).
 * <p>
 * {@code CodeCollectorSystem} is responsible for providing the required text content for the project
 * configuration files based on the build system. It can generate text for build scripts, settings files,
 * Java code, and other resources used in the project generation process.
 * </p>
 */
public enum CodeCollectorSystem {
    /**
     * Gradle build system using Groovy DSL.
     */
    GRADLE("gradle"),

    /**
     * Gradle build system using Kotlin DSL.
     */
    GRADLE_KTS("gradle (kts)");

    private final String name;

    /**
     * Constructs a {@code CodeCollectorSystem} instance with the specified name.
     *
     * @param name The name of the build system (e.g., "gradle", "gradle (kts)").
     */
    CodeCollectorSystem(String name) {
        this.name = name;
    }

    /**
     * Returns the corresponding {@code CodeCollectorSystem} enum for the given name.
     *
     * @param name The name of the build system (e.g., "gradle").
     * @return The corresponding {@code CodeCollectorSystem} enum.
     * @throws IllegalArgumentException If no matching enum is found.
     */
    public static CodeCollectorSystem get(String name) throws IllegalArgumentException {
        if (Objects.equals(name, GRADLE.name)) {
            return GRADLE;
        }
        if (Objects.equals(name, GRADLE_KTS.name)) {
            return GRADLE_KTS;
        }
        throw new IllegalArgumentException("No enum named \"" + name + "\"");
    }

    /**
     * Returns a string representation of the current {@code CodeCollectorSystem} enum constant.
     * <p>
     * This method overrides the default {@link Enum#toString()} method and returns the name of the
     * build system associated with the enum constant, which is a human-readable string.
     * The returned string represents the type of the build system, such as "gradle" or "gradle (kts)".
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * CodeCollectorSystem system = CodeCollectorSystem.GRADLE;
     * String systemName = system.toString();  // "gradle"
     * </pre>
     * </p>
     *
     * @return A string representing the name of the build system (e.g., "gradle", "gradle (kts)").
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns the group declaration for the given build system and project group.
     *
     * @param type The build system (e.g., {@link #GRADLE} or {@link #GRADLE_KTS}).
     * @param group The project group (e.g., "com.example").
     * @return The group declaration as a string.
     */
    @Contract(pure = true)
    private static @NotNull String getGroup(@NotNull CodeCollectorSystem type, String group) {
        return switch (type) {
            case GRADLE -> "group = '" + group + "'";
            case GRADLE_KTS -> "group = \"" + group + "\"";
        };
    }

    /**
     * Returns the version declaration for the given build system and project version.
     *
     * @param type The build system (e.g., {@link #GRADLE} or {@link #GRADLE_KTS}).
     * @param version The project version (e.g., "1.0.0").
     * @return The version declaration as a string.
     */
    @Contract(pure = true)
    private static @NotNull String getVersion(@NotNull CodeCollectorSystem type, String version) {
        return switch (type) {
            case GRADLE -> "version = '" + version + "'";
            case GRADLE_KTS -> "version = \"" + version + "\"";
        };
    }

    /**
     * Returns the implementation declaration for the given build system and JAR file name.
     *
     * @param type The build system (e.g., {@link #GRADLE} or {@link #GRADLE_KTS}).
     * @param name The name of the JAR file (e.g., "plugin.jar").
     * @return The implementation declaration as a string.
     */
    @Contract(pure = true)
    private static @NotNull String getImplementation(@NotNull CodeCollectorSystem type, String name) {
        return switch (type) {
            case GRADLE -> "    implementation files('libs/" + name + "')";
            case GRADLE_KTS -> "    implementation(files(\"libs/" + name + "\"))";
        };
    }

    /**
     * Retrieves the file text for the build configuration files based on the selected build system.
     *
     * @param projectGroup The project group (e.g., "com.example").
     * @param projectVersion The project version (e.g., "1.0.0").
     * @param nameJar The name of the JAR file to be included in the project.
     * @return A list of strings representing the file content.
     * @throws RuntimeException If an error occurs while reading the file content.
     */
    public List<String> getFileText(String projectGroup, String projectVersion, String nameJar) throws RuntimeException {
        StringBuilder text = new StringBuilder();
        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(CodeCollectorSystem.class.getClassLoader().getResource("CodeCollectors/" + this + ".txt")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                switch (line) {
                    case "group" -> text.append(getGroup(this, projectGroup));
                    case "version" -> text.append(getVersion(this, projectVersion));
                    case "implementation_lib" -> text.append(getImplementation(this, nameJar));
                    default -> text.append(line);
                }
                text.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Arrays.stream(text.toString().split("\n")).toList();
    }

    /**
     * Retrieves the text for the main Java file based on the project class name.
     *
     * @param className The name of the main class (e.g., "MyPlugin").
     * @return A list of strings representing the content of the main file.
     * @throws RuntimeException If an error occurs while reading the file content.
     */
    public static List<String> getMainFileText(String className) throws RuntimeException {
        StringBuilder text = new StringBuilder();
        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(CodeCollectorSystem.class.getClassLoader().getResource("CodeCollectors/main.txt")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                switch (line) {
                    case "mod_id" -> text.append("@ModId(\"").append(className).append("\")");
                    case "class" -> text.append("public class ").append(className).append(" implements Core {");
                    default -> text.append(line);
                }
                text.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Arrays.stream(text.toString().split("\n")).toList();
    }

    /**
     * Retrieves the text for the help file.
     *
     * @return A list of strings representing the content of the help file.
     * @throws RuntimeException If an error occurs while reading the file content.
     */
    public static List<String> getHelpFileText() throws RuntimeException {
        StringBuilder text = new StringBuilder();
        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(CodeCollectorSystem.class.getClassLoader().getResource("CodeCollectors/help.txt")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Arrays.stream(text.toString().split("\n")).toList();
    }

    /**
     * Retrieves the text for the Gradle wrapper script.
     *
     * @param bat A boolean flag indicating whether to return the Windows (.bat) version or not.
     * @return A list of strings representing the content of the Gradle wrapper script.
     * @throws RuntimeException If an error occurs while reading the file content.
     */
    public static List<String> getGradlewFileText(boolean bat) throws RuntimeException {
        StringBuilder text = new StringBuilder();
        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(CodeCollectorSystem.class.getClassLoader().getResource(
                    "CodeCollectors/" + getGradlewFileName(bat) + ".txt"
            )).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Arrays.stream(text.toString().split("\n")).toList();
    }

    /**
     * Returns the name of the Gradle wrapper script based on the operating system.
     *
     * @param bat A boolean flag indicating whether to return the Windows (.bat) version or not.
     * @return The name of the Gradle wrapper script.
     */
    @Contract(pure = true)
    public static @NotNull String getGradlewFileName(boolean bat) {
        return bat ? "gradlew.bat" : "gradlew";
    }

    /**
     * Returns the name of the build script file for the selected build system.
     *
     * @return The build script file name (e.g., "build.gradle" or "build.gradle.kts").
     */
    @Contract(pure = true)
    public @NotNull String getBuildName() {
        return switch (this) {
            case GRADLE -> "build.gradle";
            case GRADLE_KTS -> "build.gradle.kts";
        };
    }

    /**
     * Returns the name of the settings script file for the selected build system.
     *
     * @return The settings script file name (e.g., "settings.gradle" or "settings.gradle.kts").
     */
    @Contract(pure = true)
    public @NotNull String getSettingsName() {
        return switch (this) {
            case GRADLE -> "settings.gradle";
            case GRADLE_KTS -> "settings.gradle.kts";
        };
    }

    /**
     * Returns the path to the source directory for a given project group.
     *
     * @param projectGroup The project group (e.g., "com.example").
     * @return The path to the source directory.
     */
    @Contract(pure = true)
    public static @NotNull String getGroupPath(@NotNull String projectGroup) {
        return pathInMainRoot(String.join(
                File.separator,
                "java",
                projectGroup.replace(".", File.separator)
        ));
    }

    /**
     * Returns the path to a specific Java file in the source directory.
     *
     * @param projectGroup The project group (e.g., "com.example").
     * @param nameFile The name of the file (e.g., "Main").
     * @return The path to the Java file.
     */
    @Contract(pure = true)
    public static @NotNull String getJavaFilePath(String projectGroup, String nameFile) {
        return String.join(
                File.separator,
                getGroupPath(projectGroup),
                nameFile + ".java"
        );
    }

    /**
     * Returns the path for a given directory in the `src/main` root.
     *
     * @param name The name of the directory or file.
     * @return The full path relative to the `src/main` directory.
     */
    @Contract("_ -> new")
    public static @NotNull String pathInMainRoot(String name) {
        return String.join(File.separator, "src", "main", name);
    }
}
