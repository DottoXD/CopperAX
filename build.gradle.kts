plugins {
    id("java")
    id("com.gradleup.shadow") version "9.6.1"
}

group = "it.shulkered"
version = "0.0.9-SNAPSHOT"
description = "The freshest free anti exploit out there."
java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:26.3-R0.1-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.0.20.Final")
    implementation("com.github.retrooper:packetevents-spigot:2.14.0")
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
