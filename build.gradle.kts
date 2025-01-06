plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "9.0.0-beta2"
}

group = "net.regsirius06.engine"
version = "beta-0.2.0"
val mainClassName = "net.regsirius06.engine.Main"

repositories {
    mavenCentral()
}

application {
    mainClass.set(mainClassName)
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jetbrains:annotations:24.0.0")
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Main-Class" to mainClassName,
            "Implementation-Title" to "Engine",
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