plugins {
    kotlin("jvm") version "1.9.22"
    `java-gradle-plugin`
}

group = "xyz.wagyourtail.unimined"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val annotations by sourceSets.creating

sourceSets.main {
    compileClasspath += annotations.output
    runtimeClasspath += annotations.output
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    val annotationsImplementation by configurations.getting

    // just for Opcodes, really
    annotationsImplementation(implementation("org.ow2.asm:asm:9.7")!!)
    implementation("org.ow2.asm:asm-commons:9.7")
    implementation("org.ow2.asm:asm-tree:9.7")

    implementation(gradleApi())
}

tasks.jar {

    from(projectDir.parentFile.resolve("LICENSE.md"))

    manifest {
        attributes.putAll(
            mapOf(
                "Manifest-Version" to "1.0",
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
            )
        )
    }

    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.test {
    useJUnitPlatform()
}