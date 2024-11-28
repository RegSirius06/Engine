plugins {
    id("java")
}

group = "net.regsirius06.engine"
version = "beta-0.1.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jetbrains:annotations:24.0.0")
}

tasks.withType<Jar> {
    manifest {
        attributes(
            "Main-Class" to "net.regsirius06.engine.Main",
            "Implementation-Title" to "Engine",
            "Implementation-Version" to "beta-0.1.1"
        )
    }
}

tasks.test {
    useJUnitPlatform()
}