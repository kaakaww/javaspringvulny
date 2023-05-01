import org.jetbrains.kotlin.konan.file.File.Companion.userHome

plugins {
    kotlin("jvm") version "1.7.20"
}

val kotlinVersion = "1.7.20"

kotlin {
    sourceSets {
        main {
            kotlin { srcDirs("src") }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-script-runtime:$kotlinVersion")
    compileOnly(fileTree("$userHome/Downloads/hawkscript-sdk-3.0.11"))
    // implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223:$kotlinVersion")
}
