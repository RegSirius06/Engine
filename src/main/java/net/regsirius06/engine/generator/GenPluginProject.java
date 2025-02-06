package net.regsirius06.engine.generator;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A utility class for generating a plugin project template.
 * <p>
 * The {@code GenPluginProject} class is responsible for creating a plugin project template with a specified
 * project name, group, version, and build tool. It generates the necessary files and directories, and then
 * packages the project into ZIP and TAR archives.
 * </p>
 * <p>
 * This class performs the following tasks:
 * <ul>
 *   <li>Creates the project directory structure</li>
 *   <li>Generates configuration and build files</li>
 *   <li>Copies the necessary JAR libraries</li>
 *   <li>Generates example code</li>
 *   <li>Archives the project into ZIP and TAR formats</li>
 * </ul>
 * @see CodeCollectorSystem
 */
public final class GenPluginProject {

    /**
     * Logger instance for logging events during the process of generating a plugin project template,
     * including project creation, file copying, archiving, and error handling.
     * <p>
     * This logger is used to record key events during the creation of the plugin project template,
     * such as:
     * <ul>
     *   <li>Warnings when overwriting an existing project directory.</li>
     *   <li>Info logs when files are successfully created or copied.</li>
     *   <li>Error logs when issues occur, such as IO problems during file creation or copying.</li>
     *   <li>Messages about the project being successfully archived in ZIP and TAR formats.</li>
     * </ul>
     * <p>
     * The logger uses the SLF4J framework, providing flexibility to configure log levels (e.g., INFO, WARN, ERROR)
     * and output destinations (console, log files, etc.). This ensures that developers can track the steps of the
     * project generation process and diagnose issues as they arise.
     * <p>
     * Example log outputs:
     * <ul>
     *   <li>Informational log: "File {@code file-path} created successfully."</li>
     *   <li>Warning log: "Deleting previous root directory..."</li>
     *   <li>Error log: "Error creating ZIP archive: {@code error-message}"</li>
     * </ul>
     * <p>
     * By using this logger, users can monitor the status of the project template generation, track file operations,
     * and quickly diagnose any issues encountered during the process.
     */
    public static final Logger log = LoggerFactory.getLogger(GenPluginProject.class);

    private final String projectName;
    private final String projectGroup;
    private final String projectVersion;
    private final CodeCollectorSystem buildTool;
    private final String projectPath;
    private final String nameJar;

    /**
     * Constructs a {@code GenPluginProject} instance with the specified parameters.
     *
     * @param projectName   The name of the project (e.g., "MyPlugin").
     * @param projectGroup  The group of the project (e.g., "com.example").
     * @param projectVersion The version of the project (e.g., "1.0.0").
     * @param buildTool     The build tool used for the project (e.g., Gradle, Maven).
     * @param projectPath   The path where the project files should be created.
     * @param nameJar       The name of the JAR file to be copied to the project.
     */
    public GenPluginProject(String projectName, String projectGroup, String projectVersion,
                            CodeCollectorSystem buildTool, String projectPath, String nameJar) {
        this.projectName = projectName;
        this.projectGroup = projectGroup;
        this.projectVersion = projectVersion;
        this.buildTool = buildTool;
        this.projectPath = projectPath;
        this.nameJar = nameJar;
    }

    /**
     * Generates the plugin project template, including project structure, files, and archives.
     * This method creates the project directory, generates configuration files, copies required JARs,
     * and then creates ZIP and TAR archives of the project.
     *
     * @throws RuntimeException if there is an error during project generation.
     */
    public void generateProject() {
        File projectDir = new File(projectPath, projectName + File.separator);

        if (projectDir.exists()) {
            log.warn("Deleting previous root directory...");
            deleteDirectory(projectDir);
        }
        projectDir.mkdirs();

        new File(projectDir, String.join(
                File.separator,
                CodeCollectorSystem.getGroupPath(projectGroup),
                "executables"
        )).mkdirs();
        new File(projectDir, "libs/").mkdirs();
        new File(projectDir, "test/").mkdirs();
        new File(projectDir, CodeCollectorSystem.pathInMainRoot("resources/")).mkdirs();

        copyJarLib(projectDir);
        generateFiles(projectDir);
        generateExampleCode(projectDir);

        log.info("Project '{}' generated successfully!", projectDir.getAbsolutePath());

        checkFilesBeforeArchiving(projectDir);
        zipProject(projectDir);
        tarProject(projectDir);

        deleteDirectory(projectDir);
    }

    /**
     * Generates the necessary build and configuration files for the project based on the selected build tool.
     *
     * @param projectDir The directory where the files should be generated.
     */
    private void generateFiles(File projectDir) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(projectDir, buildTool.getBuildName())))) {
            buildTool.getFileText(projectGroup, projectVersion, nameJar).forEach(writer::println);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(projectDir, buildTool.getSettingsName())))) {
            writer.println("rootProject.name = \"" + projectName + "\"");
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        Stream.of(true, false).forEach(b -> {
            try (PrintWriter writer = new PrintWriter(new FileWriter(new File(
                    projectDir,
                    CodeCollectorSystem.getGradlewFileName(b)
            )))) {
                CodeCollectorSystem.getGradlewFileText(b).forEach(writer::println);
                writer.flush();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
    }

    /**
     * Generates example Java code files for the project.
     *
     * @param projectDir The directory where the example code files should be generated.
     */
    private void generateExampleCode(File projectDir) {
        File mainFile = new File(projectDir, CodeCollectorSystem.getJavaFilePath(projectGroup, projectName));
        try {
            if (mainFile.createNewFile()) log.info("File {} created successfully.", mainFile.getAbsolutePath());
            else log.warn("File {} already exists.", mainFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to create file {}.", mainFile.getAbsolutePath());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(mainFile))) {
            CodeCollectorSystem.getMainFileText(projectName).forEach(writer::println);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        File helpFile = new File(
                projectDir,
                CodeCollectorSystem.getJavaFilePath(projectGroup, "/executables/Help")
        );
        try {
            if (helpFile.createNewFile()) log.info("File {} created successfully.", helpFile.getAbsolutePath());
            else log.warn("File {} already exists.", helpFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to create file {}.", helpFile.getAbsolutePath());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(helpFile))) {
            CodeCollectorSystem.getHelpFileText().forEach(writer::println);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Copies the required JAR library to the project's "libs" directory.
     *
     * @param projectDir The directory where the JAR library should be copied.
     */
    private void copyJarLib(@NotNull File projectDir) {
        Path sourcePath = Paths.get("./build/libs/" + nameJar);
        Path destinationPath = Paths.get(projectDir.getPath() + "/libs/" + nameJar);

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Jar lib copied successfully!");
        } catch (IOException e) {
            log.error("Error copying jar lib: {}", e.getMessage());
        }
    }

    /**
     * Returns the name of the archive to be created, based on the project name and build tool.
     *
     * @return The archive name (e.g., "MyPlugin_Gradle").
     */
    @Contract(pure = true)
    private @NotNull String getArchiveName() {
        return projectName + "_" + buildTool;
    }

    /**
     * Checks the project directory to ensure all files are ready for archiving.
     *
     * @param projectDir The project directory to be checked.
     */
    private void checkFilesBeforeArchiving(@NotNull File projectDir) {
        Path sourcePath = projectDir.toPath();
        boolean flag = true;
        try {
            Files.walk(sourcePath)
                    .forEach(path -> {
                        if (path.toFile().isFile()) log.info("File ready for archiving: {}", path);
                        else log.info("Directory ready for archiving: {}", path);
                    });
        } catch (IOException e) {
            log.error("Error checking files before archiving: {}", e.getMessage());
            flag = false;
        }
        if (flag) log.info("All the files are ready to archive!");
    }

    /**
     * Creates a ZIP archive of the project directory.
     *
     * @param projectDir The project directory to be archived.
     */
    private void zipProject(@NotNull File projectDir) {
        File zipFile = new File(projectPath, getArchiveName() + ".zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            Path sourcePath = projectDir.toPath();

            Files.walk(sourcePath).forEach(path -> {
                try {
                    String entryName = sourcePath.relativize(path).toString();
                    if (entryName.isEmpty()) return;

                    zipOut.putNextEntry(new ZipEntry(entryName));

                    if (path.toFile().isFile()) {
                        Files.copy(path, zipOut);
                    }

                    zipOut.closeEntry();
                } catch (IOException e) {
                    log.error("Error adding file to ZIP: {}", e.getMessage());
                }
            });

            zipOut.flush();
            zipOut.close();
            log.info("Project archived as ZIP: {}", zipFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error creating ZIP archive: {}", e.getMessage());
        }
    }

    /**
     * Creates a TAR archive of the project directory.
     *
     * @param projectDir The project directory to be archived.
     */
    private void tarProject(@NotNull File projectDir) {
        File tarFile = new File(projectPath, getArchiveName() + ".tar");

        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(tarFile))) {
            Path sourcePath = projectDir.toPath();

            Files.walk(sourcePath).forEach(path -> {
                try {
                    String entryName = sourcePath.relativize(path).toString();
                    if (entryName.isEmpty()) return;

                    TarArchiveEntry entry = new TarArchiveEntry(path.toFile(), entryName);
                    tarOut.putArchiveEntry(entry);

                    if (path.toFile().isFile()) {
                        Files.copy(path, tarOut);
                    }

                    tarOut.closeArchiveEntry();
                } catch (IOException e) {
                    log.error("Error adding file to TAR: {}", e.getMessage());
                }
            });

            tarOut.flush();
            tarOut.close();
            log.info("Project archived as TAR: {}", tarFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error creating TAR archive: {}", e.getMessage());
        }
    }

    /**
     * Deletes the specified project directory and its contents.
     *
     * @param projectDir The project directory to be deleted.
     */
    public void deleteDirectory(@NotNull File projectDir) {
        Path dirPath = projectDir.toPath();
        try {
            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            log.info("The directory {} deleted successfully!", projectDir.getPath());
        } catch (IOException e) {
            log.error("Error while deleting temporal files: {}", e.getMessage());
        }
    }
}
