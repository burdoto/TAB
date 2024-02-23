plugins {
    id("fabric-loom")
}

repositories {
    // Gradle doesn't support combining settings and project repositories, so we have to re-declare all the settings repos we need
    maven("https://jitpack.io") // YamlAssist
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://repo.viaversion.com/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(projects.shared)
    minecraft("com.mojang:minecraft:24w07a")
    mappings(loom.officialMojangMappings())
    modImplementation("me.lucko:fabric-permissions-api:0.2-SNAPSHOT")
    modImplementation("net.fabricmc:fabric-loader:0.15.5")
    val version = "0.83.1+1.20.1"
    modImplementation(fabricApi.module("fabric-api-base", version))
    modImplementation(fabricApi.module("fabric-lifecycle-events-v1", version))
    modImplementation(fabricApi.module("fabric-networking-api-v1", version))
    modImplementation(fabricApi.module("fabric-entity-events-v1", version))
    modImplementation(fabricApi.module("fabric-command-api-v1", "0.77.0+1.18.2"))
    modImplementation(fabricApi.module("fabric-command-api-v2", version))
}

tasks {
    compileJava {
        options.release.set(17)
    }
}
