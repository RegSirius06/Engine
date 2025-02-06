/**
 * The {@code net.regsirius06.engine.generator} package contains classes and enums related to generating
 * configuration and project files for various build systems, such as Gradle (using Groovy and the Kotlin DSL).
 * <p>
 * This package introduces the {@link net.regsirius06.engine.generator.CodeCollectorSystem} class, which provides
 * methods for extracting the text needed to generate various project files,
 * such as build files, settings, main class, help files, and Gradle wrapper scripts. These files are used to set up projects by automatically
 * creating configurations, dependencies, and other resources for different build systems.
 * </p>
 * <p>
 * The {@link net.regsirius06.engine.generator.CodeCollectorSystem} class allows you to choose a build system (Gradle or
 * Gradle Kotlin DSL) and generate appropriate strings and files, taking into
 * account the specifics of each system.
 * </p>
 * <p>
 * Another key component of this package is the {@link net.regsirius06.engine.generator.GenPluginProject} class.
 * It is responsible for the actual generation of the plugin project, including creating project directories,
 * generating the necessary configuration files (such as build files and settings), and preparing the project for archiving.
 * This class relies on the data provided by {@link net.regsirius06.engine.generator.CodeCollectorSystem} to populate project files
 * with the appropriate content.
 * </p>
 * <p>
 * Additionally, {@link net.regsirius06.engine.generator.CodeCollectorSystem} provides utility methods for managing file paths
 * within the project structure, creating Java files, and handling auxiliary resources required for the generated plugin project.
 * </p>
 */
package net.regsirius06.engine.generator;
