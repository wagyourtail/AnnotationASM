package xyz.wagyourtail.asm.annotations;

import xyz.wagyourtail.asm.annotations.md.Parameter;
import xyz.wagyourtail.asm.annotations.md.ParameterAnnotation;
import xyz.wagyourtail.asm.annotations.md.InsnNode;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;
import xyz.wagyourtail.asm.annotations.ref.MethodRef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface MethodASM {

    /**
     * if the method access should be changed.
     */
    int access() default 0;

    /**
     * if the method name should be changed.
     */
    String name() default "";

    /**
     * if the method descriptor should be changed.
     */
    MethodRef.Desc desc() default @MethodRef.Desc(returnType = @ClassRef(), args = {});

    /**
     * if the method signature should be changed.
     */
    String signature() default "";

    /**
     * if the method exceptions should be added to.
     */
    ClassRef[] addExceptions() default {};

    /**
     * if the method exceptions should be removed from.
     */
    ClassRef[] removeExceptions() default {};

    /**
     * if any parameters should be changed.
     */
    Parameter[] parameters() default {};

    /**
     * if any visible annotations should be added.
     */
    AnnotationASM[] addVisibleAnnotations() default {};

    /**
     * if any invisible annotations should be added.
     */
    AnnotationASM[] addInvisibleAnnotations() default {};

    /**
     * if any visible annotations should be removed.
     */
    ClassRef[] removeVisibleAnnotations() default {};

    /**
     * if any invisible annotations should be removed.
     */
    ClassRef[] removeInvisibleAnnotations() default {};

    /**
     * if any visible type annotations should be added.
     */
    AnnotationASM[] addVisibleTypeAnnotations() default {};

    /**
     * if any invisible type annotations should be added.
     */
    AnnotationASM[] addInvisibleTypeAnnotations() default {};

    /**
     * if any visible type annotations should be removed.
     */
    ClassRef[] removeVisibleTypeAnnotations() default {};

    /**
     * if any invisible type annotations should be removed.
     */
    ClassRef[] removeInvisibleTypeAnnotations() default {};

    /**
     * set the default for annotation methods
     */
    AnnotationASM.Value annotationDefault() default @AnnotationASM.Value();

    /**
     * change the number of visible annotable parameters.
     */
    int visibleAnnotableParameterCount() default 0;

    /**
     * visible Parameter annotations
     */
    ParameterAnnotation[] visibleParameterAnnotations() default {};

    /**
     * change the number of invisible annotable parameters.
     */
    int invisibleAnnotableParameterCount() default 0;

    /**
     * invisible Parameter annotations
     */
    ParameterAnnotation[] invisibleParameterAnnotations() default {};

    /**
     * if the code should be replaced.
     */
    InsnNode[] code() default {};

}
