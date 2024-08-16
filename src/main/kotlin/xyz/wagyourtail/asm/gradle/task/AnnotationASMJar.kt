@file:Suppress("LeakingThis")

package xyz.wagyourtail.asm.gradle.task

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import xyz.wagyourtail.asm.PathProcessor
import xyz.wagyourtail.asm.gradle.utils.openZipFileSystem
import xyz.wagyourtail.asm.gradle.utils.FinalizeOnRead
import xyz.wagyourtail.asm.gradle.utils.MustSet

abstract class AnnotationASMJar : Jar() {

    @get:InputFiles
    var inputFiles: FileCollection by FinalizeOnRead(MustSet())

    @TaskAction
    fun doTransform() {
        for (input in inputFiles) {
            if (input.isDirectory) {
                val output = temporaryDir.resolve(input.name + "-asm")
                PathProcessor.process(input.toPath(), output.toPath())
                from(output)
            } else if (input.extension == "jar") {
                val output = temporaryDir.resolve(input.nameWithoutExtension + "-expect-platform." + input.extension)
                input.toPath().openZipFileSystem().use { inputFs ->
                    output.toPath().openZipFileSystem(mapOf("create" to true)).use { outputFs ->
                        PathProcessor.process(
                            inputFs.getPath("/"),
                            outputFs.getPath("/")
                        )
                    }
                }
                from(project.zipTree(output))
            } else if (input.exists()) {
                throw IllegalStateException("AnnotationASMJar: $input is not a directory or jar file")

            }
        }

        copy()
    }

}
