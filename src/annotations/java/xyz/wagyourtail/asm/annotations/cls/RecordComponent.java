package xyz.wagyourtail.asm.annotations.cls;

import xyz.wagyourtail.asm.annotations.AnnotationASM;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface RecordComponent {

    String name();

    ClassRef desc();

    String signature() default "";

    AnnotationASM[] visibleAnnotations() default {};

    AnnotationASM[] invisibleAnnotations() default {};

    AnnotationASM[] visibleTypeAnnotations() default {};

    AnnotationASM[] invisibleTypeAnnotations() default {};

}
