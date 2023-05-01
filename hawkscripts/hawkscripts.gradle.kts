import org.jetbrains.kotlin.konan.file.File.Companion.userHome

plugins {
    kotlin("jvm") version "1.7.20"
}

val kotlinVersion = "1.7.20"
val hawkScriptSdkVersion = "3.0.11"

kotlin {
    sourceSets {
        main {
            kotlin {
                srcDirs(
                    "authentication",
                    "session",
                    "httpsender",
                    "active",
                    "proxy",
                )
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-script-runtime:$kotlinVersion")
    // change to location of the hawkscript-sdk-<version>/ directory.
    compileOnly(fileTree("$userHome/Downloads/hawkscript-sdk-$hawkScriptSdkVersion"))
}
