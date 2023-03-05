
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("com.adarshr.test-logger") version "3.0.0"
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

//tasks.test {
//    useJUnitPlatform()
//}

tasks.test {
//    environment("HTTP_PROXY" to System.getenv("HTTP_PROXY"))
    useJUnitPlatform()
}

testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
    showExceptions = true
    showStackTraces = true
    showFullStackTraces = true
    showCauses = true
}