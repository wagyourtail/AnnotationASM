package xyz.wagyourtail.asm.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import xyz.wagyourtail.unimined.expect.task.AnnotationASMJar

abstract class AnnotationASMPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.extensions.create("annotationASM", AnnotationASMExtension::class.java)

        target.tasks.register("annotationASMJar", AnnotationASMJar::class.java) {
            val sourceSets = target.extensions.getByType(SourceSetContainer::class.java)
            it.from(sourceSets.getByName("main").output)
        }
    }

}