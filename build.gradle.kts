import java.net.URI

plugins {
    kotlin("jvm") version "1.9.22"
    `java-gradle-plugin`
    `maven-publish`
}

group = "xyz.wagyourtail.annotationasm"
version = if (project.hasProperty("version_snapshot")) project.properties["version"] as String + "-SNAPSHOT" else project.properties["version"] as String


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

sourceSets.test {
    compileClasspath += processors.output
    runtimeClasspath += processors.output
}

base {
    archivesName.set("annotationasm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

val processAnnotations by tasks.registering(JavaExec::class) {
    dependsOn(tasks.getByName("annotationsClasses"))
    classpath = processors.runtimeClasspath
    mainClass = "xyz.wagyourtail.asm.PathProcessor"
    val output = temporaryDir.resolve("annotations")
    outputs.dir(output)
    args = listOf(
        annotations.output.files.joinToString(File.pathSeparator) { it.absolutePath },
        output.absolutePath
    )
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(processAnnotations.get().outputs.files)

    val annotationsApi by configurations.getting

    // just for Opcodes, really
    annotationsApi(api("org.ow2.asm:asm:9.7")!!)
    implementation("org.ow2.asm:asm-commons:9.7")
    implementation("org.ow2.asm:asm-tree:9.7")
    compileOnly("org.ow2.asm:asm-util:9.7")

    implementation(gradleApi())
}

val annotationsJar by tasks.registering(Jar::class) {
    dependsOn(processAnnotations)
    archiveBaseName.set("annotationasm-annotations")
    from(processAnnotations.get().outputs.files)
}

tasks.jar {
    from(projectDir.parentFile.resolve("LICENSE.md"))
    from(processAnnotations.get().outputs.files)
    from(processors.output)

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

val processTest by tasks.registering(JavaExec::class) {
    dependsOn(tasks.getByName("testClasses"))
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass = "xyz.wagyourtail.asm.PathProcessor"
    val output = temporaryDir.resolve("test")
    outputs.dir(output)
    args = listOf(
        sourceSets.test.get().output.files.joinToString(File.pathSeparator) { it.absolutePath },
        output.absolutePath
    )
}

tasks.compileTestJava {
    javaCompiler = javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.test {
    executable = "/usr/lib/jvm/java-17-openjdk/bin/java"
    dependsOn(processTest)
    useJUnitPlatform()
    classpath = sourceSets.test.get().runtimeClasspath - sourceSets.test.get().output + processTest.get().outputs.files
}

tasks.assemble {
    dependsOn(annotationsJar)
}

gradlePlugin {
    plugins {
        create("annotationasm") {
            id = "xyz.wagyourtail.annotationasm"
            implementationClass = "xyz.wagyourtail.asm.gradle.AnnotationASMPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "WagYourMaven"
            url = if (project.hasProperty("version_snapshot")) {
                URI.create("https://maven.wagyourtail.xyz/snapshots/")
            } else {
                URI.create("https://maven.wagyourtail.xyz/releases/")
            }
            credentials {
                username = project.findProperty("mvn.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("mvn.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("annotations") {
            groupId = project.group.toString()
            artifactId = "annotationasm-annotations"
            version = project.version.toString()

            artifact(tasks.getByName("annotationsJar"))
        }
    }
}
