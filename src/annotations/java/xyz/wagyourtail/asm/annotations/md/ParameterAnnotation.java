package xyz.wagyourtail.asm.annotations.md;

import xyz.wagyourtail.asm.annotations.AnnotationASM;

public @interface ParameterAnnotation {
    int index();

    AnnotationASM[] annotations() default {};
}
