import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import kotlin.math.roundToLong

plugins {
    kotlin("jvm") version "1.8.22"
}

val kotlinVersion = "1.7.20"
val hawkScriptSdkVersion = lazy { sdkVersion() }
val sdkZipName = lazy {  "hawkscript-sdk-${hawkScriptSdkVersion.value}.zip" }
val hawkScriptSDKZip = lazy { "$buildDir/${sdkZipName.value}" }

tasks.compileKotlin.configure {
    if (!File(hawkScriptSDKZip.value).exists()) {
        logger.warn("hawkscripts sdk zip ${hawkScriptSDKZip.value} not found")
        logger.lifecycle("Run ./gradlew :hawkscripts:download to enable kotlin scripting support for IntelliJ")
    }
    enabled = File(hawkScriptSDKZip.value).exists()
}

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
    compileOnly(zipTree(hawkScriptSDKZip.value))
}
tasks.register("download") {

    group = "StackHawk"
    description = "Download the latest hawk scripting sdk zip"

    doLast {

        Files.createDirectories(buildDir.toPath())
        val localSdkZip = File("$buildDir/${sdkZipName.value}")
        if (!localSdkZip.exists()) {
            val hawkscriptSdkUrl = URL("https://download.stackhawk.com/hawk/sdk/${sdkZipName.value}")
            val zipConn = hawkscriptSdkUrl.openConnection() as HttpURLConnection
            zipConn.connect()
            if ((200..299).contains(zipConn.responseCode)) {
                logger.lifecycle("Downloading $hawkscriptSdkUrl -> $localSdkZip")
                val delay = 1000L
                var curLen = 0
                val len = zipConn.getHeaderField("Content-Length").toLong()
                val input = zipConn.inputStream
                localSdkZip.outputStream().use { output ->
                    var buf = ByteArray(8192)
                    var c = input.read(buf, 0, buf.size)
                    var lastPct = 0L
                    while (c != -1) {
                        curLen += c
                        output.write(buf, 0, c)
                        c = input.read(buf, 0, buf.size)
                        val pc = ((curLen.toDouble() / len.toDouble()) * 100).roundToLong()
                        if ((System.currentTimeMillis() % delay) == 0L && lastPct != pc) {
                            logger.lifecycle("${sdkZipName.value} [${curLen / 1024 / 1024}mb] ${pc}%")
                            lastPct = pc
                        }
                    }
                    val pc = ((curLen.toDouble() / len.toDouble()) * 100).roundToLong()
                    logger.lifecycle("${sdkZipName.value} [${curLen / 1024 / 1024}mb] ${pc}%")
                }
            } else {
                logger.error("Error downloading $hawkscriptSdkUrl ${zipConn.responseMessage}")
            }
        } else {
            logger.lifecycle("latest hawkscan sdk already found: ${"$buildDir/${sdkZipName.value}"}")
        }

    }
}

fun sdkVersion(): String {
    val verFile = File("$buildDir/hawkscriptsdk.version")
    val ret = if (verFile.exists()) {
        verFile.readText()
    } else {
        downloadSdkVersion()
        verFile.readText()
    }
    return ret
}

fun downloadSdkVersion() {
    Files.createDirectories(buildDir.toPath())
    val hawkscanVersionUrl = URL("https://api.stackhawk.com/hawkscan/version")
    val verisionConn = hawkscanVersionUrl.openConnection() as HttpURLConnection
    verisionConn.connect()
    val version = String(verisionConn.inputStream.readAllBytes())
    File("$buildDir/hawkscriptsdk.version").outputStream().use {
        it.write(version.toByteArray())
    }
}

