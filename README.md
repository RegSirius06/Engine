# Engine
Graphics engine by my own

## How to play?

Java 17 is used.

To use it, download the jar file from the latest release and run it with command:

```bash
java -jar "path_to_file/file.jar"
```

You can use it without JDK. You must run `bin/Egine.bat` in archive with game.

## How to use?

### Option 1: Adding a JAR to IntelliJ IDEA

1. Open the project where you want to use the library.
2. Go to File > Project Explorer (Ctrl+Alt+Shift+S).
3. In the Modules section, select Dependencies.
4. Click + and select JAR files or directories.
5. Select the generated JAR file from your first project and add it as a dependency.
6. Click OK to make the changes.

Now you can use the classes from this library in your project.

### Option 2. Using Maven or Gradle to include the library.

If you are using Maven or Gradle, you can include the library as a constraint by adding it to the project configuration.

1. For Maven:
    * Place the JAR file in your project directory (e.g. libs).
    * In pom.xml, add the dependency:
    ```xml
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>my-library</artifactId>
        <version>1.0</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/libs/Engine-beta-0.2.0.jar</systemPath>
    </dependency>
    ```

2. For Gradle:
   * Place the JAR file in your project directory (e.g. libs).
   * In the `build.gradle` file, add the dependency:
   ```gradle
   dependencies {
    implementation files('libs/Engine-beta-0.2.0.jar')
   }
   ```

3. For Gradle (KTS):
   * Place the JAR file in your project directory (e.g. libs).
   * In the `build.gradle.kts` file, add the dependency:
   ```kotlin
   dependencies {
    implementation(files("libs/Engine-beta-0.2.0.jar"))
   }
   ```

Since this is just a beta version, I will not publish it to GitHub Packages or Maven Central. When the project becomes large enough, then it will make sense.

## Docs

If you want to read the documentation you can do it [here](https://regsirius06.github.io/Engine/).
