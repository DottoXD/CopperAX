plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.2"
}

group = "it.shulkered"
version = "0.0.6-SNAPSHOT"
description = "The freshest free anti exploit out there."
java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.2-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.0.20.Final")
    implementation("com.github.retrooper:packetevents-spigot:2.11.2")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    minimize()
    archiveFileName.set("${project.name}-${project.version}.jar")
    relocate("io.github.retrooper.packetevents", "it.shulkered.CopperAX.shaded.io.github.retrooper.packetevents")
    relocate("com.github.retrooper.packetevents", "it.shulkered.CopperAX.shaded.com.github.retrooper.packetevents")
}
