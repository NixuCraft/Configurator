plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

repositories {
    maven("https://jitpack.io")
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.viaversion.com")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // Server
    compileOnly("org.github.paperspigot:paperspigot-api:1.8.8-R0.1-SNAPSHOT")
    // iirc below include NMS
    // compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    // compileOnly("org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT")

    // Lombok (QOL)
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Custom libs
    val libsPath: String by project
    implementation(files("${libsPath}/NixUtils-1.0.0-all.jar"))
}

tasks.build {
    dependsOn("shadowJar")
    doLast {
        val exportPath: String by project
        val buildJar = File("${projectDir}/build/libs", "${rootProject.name}-${rootProject.version}-all.jar")

        buildJar.copyTo(File(exportPath, buildJar.name), true)
    }
}