package net.regsirius06.engine.plugins.files;

import net.regsirius06.engine.API.annotations.ImplementationOf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A utility class responsible for generating files that contain information
 * about classes annotated with {@link ImplementationOf}. The generated files
 * are placed in the META-INF/services directory, following the ServiceLoader pattern.
 *
 * <p>This class scans the provided root directory for annotated classes and
 * generates the appropriate service files. These files are used to load plugins
 * during runtime.</p>
 *
 * <p>It handles both file creation and writing, ensuring that the necessary
 * directories and files are present and up to date.</p>
 */
public class PluginDataProvider {

    /**
     * Logger instance for logging important events and error messages.
     *
     * <p>Uses SLF4J to provide logging functionality. Messages include
     * information about file generation progress, errors encountered, and
     * successful operations.</p>
     */
    public static final Logger log = LoggerFactory.getLogger(PluginDataProvider.class);
    private final String root;

    /**
     * Private constructor to initialize the data provider with a root directory.
     *
     * @param root the root directory where classes will be scanned for annotations
     */
    private PluginDataProvider(@Nullable String root) {
        this.root = root;
    }

    /**
     * Creates a new instance of {@link PluginDataProvider} with the specified root directory.
     *
     * @param root the root directory to scan for classes
     * @return a new {@link PluginDataProvider} instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull PluginDataProvider of(String root) {
        return new PluginDataProvider(root);
    }

    /**
     * Scans for classes annotated with {@link ImplementationOf} and generates service
     * files in the META-INF/services directory.
     * <p>
     * This method ensures that all necessary directories are created and any existing
     * files are properly cleared and updated with the new class names.
     * </p>
     */
    public void createDataFiles() {
        Reflections reflections;
        try {
            reflections = new Reflections(root, Scanners.TypesAnnotated);
        } catch (NullPointerException e) {
            if (log != null) {
                log.error("Failed to process path: {}", root);
            }
            return;
        }

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(ImplementationOf.class);

        if (log != null) {
            log.info("File generation started.");
        }

        if (annotatedClasses.isEmpty()) {
            if (log != null) {
                log.warn("No classes found to generate files.");
            }
            finishMessage();
            return;
        }

        Map<String, FileWriter> fileWriterMap = new HashMap<>();
        Map<String, Boolean> needToClear = new HashMap<>();

        // Generate and write data for each annotated class
        for (Class<?> clazz : annotatedClasses) {
            if (log != null) {
                log.info("Found class: {}", clazz.getName());
            }
            Class<?> pluginInterface = clazz.getAnnotationsByType(ImplementationOf.class)[0].value();
            try {
                Path metaInfPath = Path.of("src/main/resources/META-INF/services");

                // Ensure META-INF directory exists
                if (!Files.exists(metaInfPath)) {
                    if (log != null) {
                        log.warn("Directories don't exist. Trying to create directories...");
                    }
                    try {
                        Files.createDirectories(metaInfPath);
                    } catch (IOException e) {
                        throw new CommentedIOException(e, "Directories were not created.");
                    }
                    if (log != null) {
                        log.info("Directories created successfully.");
                    }
                }

                Path serviceFilePath = metaInfPath.resolve(pluginInterface.getName());

                File serviceFile = serviceFilePath.toFile();

                // Create the service file if it doesn't exist
                if (!serviceFile.exists()) {
                    if (log != null) {
                        log.warn("File doesn't exist. Trying to create file...");
                    }
                    try {
                        serviceFile.createNewFile();
                    } catch (IOException e) {
                        throw new CommentedIOException(e, "File was not created.");
                    }
                    if (log != null) {
                        log.info("File created successfully.");
                    }
                } else if (needToClear.getOrDefault(serviceFile.getAbsolutePath(), true)) {
                    try (FileWriter writer = new FileWriter(serviceFile, false)) {
                        writer.write("");
                        needToClear.put(serviceFile.getAbsolutePath(), false);
                    } catch (IOException e) {
                        throw new CommentedIOException(e, "Failed to clear file.");
                    }
                }

                // Write the class name to the file
                try {
                    if (!fileWriterMap.containsKey(serviceFile.getAbsolutePath())) {
                        try {
                            fileWriterMap.put(serviceFile.getAbsolutePath(), new FileWriter(serviceFile, true));
                        } catch (IOException e) {
                            throw new CommentedIOException(e, "Failed to create data writer for the file.");
                        }
                    }
                    fileWriterMap.get(serviceFile.getAbsolutePath()).write(clazz.getName() + "\n");
                } catch (IOException e) {
                    throw new CommentedIOException(e, "Failed to write data to the file.");
                }

                if (log != null) {
                    log.info("The record was created for the class: {}", clazz.getName());
                }
            } catch (CommentedIOException e) {
                if (log != null) {
                    log.error(e.getCommentedStackTrace());
                } else {
                    e.getIOException().printStackTrace();
                }
            }
        }

        // Ensure all files are written and flushed
        for (Map.Entry<String, FileWriter> writer : fileWriterMap.entrySet()) {
            try {
                writer.getValue().flush();
            } catch (IOException e) {
                if (log != null) {
                    log.error("Failed to write file: {}", writer.getKey());
                }
            }
        }

        finishMessage();
    }

    /**
     * Final message after file generation is completed.
     */
    protected static void finishMessage() {
        if (log != null) {
            log.info("File generation completed.");
        }
    }
    /**
     * Custom {@link IOException} subclass with an additional comment to provide
     * context about the error.
     */
    public static class CommentedIOException extends IOException {

        /**
         * A descriptive comment providing additional context about the exception.
         */
        private final String comment;

        /**
         * The original {@link IOException} that triggered this exception.
         */
        private final IOException e;

        /**
         * Constructs a new {@link CommentedIOException}.
         *
         * @param e       the original {@link IOException}
         * @param comment the comment providing additional context
         */
        public CommentedIOException(IOException e, String comment) {
            this.comment = comment;
            this.e = e;
        }

        /**
         * Retrieves the comment associated with this exception.
         *
         * @return the comment
         */
        public String getComment() {
            return comment;
        }

        /**
         * Retrieves the underlying {@link IOException}.
         *
         * @return the original {@link IOException}
         */
        public IOException getIOException() {
            return e;
        }

        /**
         * Retrieves a stack trace with the additional comment.
         *
         * @return the commented stack trace
         */
        public String getCommentedStackTrace() {
            return comment + "\nHere is stacktrace:\n" + e.fillInStackTrace();
        }
    }
}
