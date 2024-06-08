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

val processors by sourceSets.creating {
    compileClasspath += sourceSets.main.get().compileClasspath + annotations.output
    runtimeClasspath += sourceSets.main.get().runtimeClasspath + annotations.output
}

sourceSets.main {
    compileClasspath += annotations.output + processors.output
    runtimeClasspath += annotations.output + processors.output
}

base {
    archivesName.set("annotationasm")
}

java {

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
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

val processAnnotations by tasks.registering(JavaExec::class) {
    dependsOn(tasks.getByName("annotationsClasses"))
    classpath = processors.runtimeClasspath
    mainClass = "xyz.wagyourtail.asm.PathProcessor"
    val output = temporaryDir.resolve("annotations")
    outputs.dir(output)
    args = listOf(
        annotations.output.classesDirs.files.joinToString(File.pathSeparator) { it.absolutePath },
        output.absolutePath
    )
}

val annotationsJar by tasks.registering(Jar::class) {
    dependsOn(processAnnotations)
    archiveBaseName.set("annotationasm-annotations")
    from(processAnnotations.get().outputs.files)
}

tasks.test {
    useJUnitPlatform()
}