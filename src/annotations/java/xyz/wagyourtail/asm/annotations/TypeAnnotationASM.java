package xyz.wagyourtail.asm.annotations;

public @interface TypeAnnotationASM {
    int typeRef();
    String typePath() default  "";
    AnnotationASM annotation();
}
