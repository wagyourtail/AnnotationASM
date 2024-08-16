package xyz.wagyourtail.asm.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import xyz.wagyourtail.asm.gradle.task.AnnotationASMJar

abstract class AnnotationASMPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val ext = target.extensions.create("annotationASM", AnnotationASMExtension::class.java)

        target.configurations.named("implementation").configure {
            it.dependencies.add(target.dependencies.create(ext.annotationsDep))
        }

        target.tasks.register("annotationASMJar", AnnotationASMJar::class.java) {
            it.inputFiles = target.tasks.named("jar").get().outputs.files
        }
    }

}