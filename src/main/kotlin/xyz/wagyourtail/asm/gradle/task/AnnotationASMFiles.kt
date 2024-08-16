package xyz.wagyourtail.asm.gradle.task

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import xyz.wagyourtail.asm.PathProcessor
import xyz.wagyourtail.asm.gradle.utils.openZipFileSystem
import xyz.wagyourtail.asm.gradle.utils.FinalizeOnRead
import xyz.wagyourtail.asm.gradle.utils.MustSet
import java.io.File
import java.nio.file.FileSystem
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.name

abstract class AnnotationASMFiles : ConventionTask() {

    @get:InputFiles
    var inputCollection: FileCollection by FinalizeOnRead(MustSet())

    @get:Internal
    val outputMap: Map<File, File>
        get() = inputCollection.associateWith { temporaryDir.resolve(it.name) }

    /**
     * this is the true output, gradle just doesn't have a
     * \@OutputDirectoriesAndFiles
     */
    @get:Internal
    val outputCollection: FileCollection by lazy {
        val fd = inputCollection.map { it to temporaryDir.resolve(it.name) }

        outputs.dirs(*fd.filter { it.first.isDirectory }.map { it.second }.toTypedArray())
        outputs.files(*fd.filter { it.first.isFile }.map { it.second }.toTypedArray())

        outputs.files
    }

    @TaskAction
    fun doTranform() {
        var toTransform = inputCollection.map { it.toPath() }.filter { it.exists() }

        val fileSystems = mutableSetOf<FileSystem>()

        try {

            outputs.files.forEach { it.deleteRecursively() }

            val transformed = toTransform.map { temporaryDir.resolve(it.name) }.map {
                if (it.extension == "jar" || it.extension == "zip") {
                    val fs = it.toPath().openZipFileSystem(mapOf("create" to true))
                    fileSystems.add(fs)
                    fs.getPath("/")
                } else it.toPath()
            }

            toTransform = toTransform.map {
                if (it.isDirectory()) it else run {
                    val fs = it.openZipFileSystem()
                    fileSystems.add(fs)
                    fs.getPath("/")
                }
            }
            for (i in toTransform.indices) {
                val input = toTransform[i]
                val output = transformed[i]
                PathProcessor.process(input, output)
            }
        } finally {
            fileSystems.forEach { it.close() }
        }
    }

    fun forInputs(files: Set<File>): FileCollection {
        return project.files(outputMap.filterKeys { it in files }.values)
    }

}