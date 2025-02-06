# Engine

## Overview
Engine is a graphics engine focused on generating and rendering mazes using the ray-casting method. It also includes lighting effects to enhance visual representation.

With the beta-1.0.0 update, Engine now supports modifications via plugins, allowing developers to:
- Add new types of maze walls
- Introduce new maze generation algorithms
- Implement additional tasks, including integrating entirely separate projects as plugins

The engine also provides built-in plugin description pages, including documentation for the `Base` plugin and other installed plugins.

**Full documentation is available [here](https://regsirius06.github.io/Engine/).**

---

## How to Run
### Requirements
- Java Development Kit (JDK) 17 is required to run the engine.
- Alternatively, a pre-compiled version that does not require Java can be downloaded from the [Releases](https://github.com/RegSirius06/Engine/releases/) page.

### Running the Engine
1. Download the latest release.
2. Extract the archive.
3. Navigate to the `bin` folder.
4. Use the appropriate script for your operating system:
   - **Windows:** Run `Engine.bat`
   - **Linux/macOS:** Run `Engine`

Alternatively, you can execute the JAR file directly with:
```bash
java -jar "path_to_file/file.jar"
```

---

## Dependencies
The project relies on the following libraries:
```gradle
dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("org.reflections:reflections:0.10.2")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}
```
These dependencies are automatically handled when using the provided templates.

---

## Examples
In the `/examples/` directory of the repository, you can find ready-made templates for developing your own projects. Each template includes all necessary configurations and dependencies, so you can start coding immediately. Just extract the required template and begin development.

To use plugins, place them in the `plugins/` directory of the main application. It is recommended to use compiled plugins labeled as `fat`.

---

## API & Configuration
For information on using the API and configuring the engine, refer to the **API section** in the [documentation](https://regsirius06.github.io/Engine/).

---

## License
This project is licensed under the **GNU General Public License (GPL)**. The full license text is available in the repository.

---

## Contributing
Developers can contribute by creating and sharing their own plugins. To submit a plugin:
1. Upload your plugin to GitHub.
2. Open an **Issue** in this repository.
3. Provide a description of your plugin and include a link to its repository.
4. Make sure to add the **plugin** label to your Issue.

---

## Contact
For questions, bug reports, or feature requests, open an issue in the repository or check the documentation for further details.
