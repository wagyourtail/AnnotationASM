package xyz.wagyourtail.asm.gradle

abstract class AnnotationASMExtension {
    val version: String = AnnotationASMExtension::class.java.`package`.implementationVersion ?: "1.0.0-SNAPSHOT"
    val annotationsDep: String = "xyz.wagyourtail.annotationasm:annotationasm-annotations:$version"
}