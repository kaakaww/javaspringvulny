import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    idea
    distribution
    id("org.springframework.boot") version "2.7.18"
}
apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")
    implementation("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    implementation("io.jsonwebtoken:jjwt-api:0.10.7")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")

    implementation("org.springdoc:springdoc-openapi-ui:1.8.0")

    testCompileOnly("junit:junit")

    implementation("io.jsonwebtoken:jjwt-impl:0.10.7")
    implementation("io.jsonwebtoken:jjwt-jackson:0.10.7")
    implementation("io.resurface:resurfaceio-logger:2.2.0")
    implementation("org.apache.commons:commons-compress:1.27.1")

    compileOnly("org.projectlombok:lombok:1.18.10")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

configurations.implementation {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

tasks.named<BootJar>("bootJar") {
    archiveBaseName.set("java-spring-vuly")
    archiveVersion.set("0.2.0")
}
