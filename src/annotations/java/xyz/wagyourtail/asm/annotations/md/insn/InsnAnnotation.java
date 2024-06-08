package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.AnnotationASM;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface InsnAnnotation {
    int typeRef();
    // empty == null
    String typePath() default "";
    boolean visible() default true;

    AnnotationASM annotation();
}
