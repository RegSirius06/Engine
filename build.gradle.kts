plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "9.0.0-beta2"
}

group = "net.regsirius06.engine"
version = "beta-1.0.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("net.regsirius06.engine.tasks.RunApp")
    applicationDefaultJvmArgs = listOf(
        "-Dproject.fileName=${rootProject.name}-${rootProject.version}.jar"
    )
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("org.reflections:reflections:0.10.2")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

tasks.register<JavaExec>("initPlugins") {
    group = "run"

    mainClass.set("net.regsirius06.engine.tasks.InitPlugins")
    classpath = sourceSets["main"].runtimeClasspath

    args = listOf(project.group.toString())
}

tasks.register<JavaExec>("initProject") {
    group = "run"

    mainClass.set("net.regsirius06.engine.tasks.InitProject")
    classpath = sourceSets["main"].runtimeClasspath

    args = listOf(project.name + "-" + project.version.toString() + ".jar", project.version.toString())
}

tasks.register<JavaExec>("runApp") {
    group = "run"
    
    mainClass.set("net.regsirius06.engine.tasks.RunApp")
    classpath = sourceSets["main"].runtimeClasspath

    args = listOf()
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Main-Class" to "net.regsirius06.engine.tasks.RunApp",
            "Implementation-Title" to rootProject.name,
            "Implementation-Version" to version
        )
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    manifest {
        from(tasks.named<Jar>("jar").get().manifest)
    }
    archiveClassifier.set("fat")
}

tasks.test {
    useJUnitPlatform()
}