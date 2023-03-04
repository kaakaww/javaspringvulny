
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {

    implementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.5.0")
    testImplementation("io.github.bonigarcia:webdrivermanager:5.3.0")
}

tasks.test {
    useJUnitPlatform()
}
