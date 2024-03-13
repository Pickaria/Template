plugins {
    id("java")
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    id("xyz.jpenilla.run-paper") version "2.2.3"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
}

group = "fr.pickaria"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pickaria.fr/releases")
    maven("https://maven.pickaria.fr/snapshot")
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    testImplementation(kotlin("test"))
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.78.0")
    testImplementation("io.mockk:mockk:1.13.8")
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain(17)
}

tasks {
    test {
        useJUnitPlatform()
    }

    runServer {
        minecraftVersion("1.20.4")
    }

    jar {
        archiveFileName.set("${project.name}.jar")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        destinationDirectory.set(file("run/plugins"))

        manifest {
            attributes["Main-Class"] = "fr.pickaria.MainKt"
        }

        from(configurations.runtimeClasspath.get().map { zipTree(it) })
    }
}

publishing {
    repositories {
        maven {
            name = "pickariaRepository"
            url = uri(
                "https://maven.pickaria.fr/${if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"}"
            )
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name
            from(components["java"])
        }
    }
}
