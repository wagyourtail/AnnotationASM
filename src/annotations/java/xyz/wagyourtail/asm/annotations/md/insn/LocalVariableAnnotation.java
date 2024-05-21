package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface LocalVariableAnnotation {
    int typeRef();
    String typePath() default "";
    String[] startLabels();
    String[] endLabels();
    int[] indexes();
    ClassRef desc();
    boolean visible() default true;
}
