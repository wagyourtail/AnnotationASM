package xyz.wagyourtail.asm.annotations;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface InnerClass {
    String name();

    ClassRef outerName() default @ClassRef;

    String innerName() default "";

    int access();
}
