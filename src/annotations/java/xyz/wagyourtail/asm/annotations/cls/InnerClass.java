package xyz.wagyourtail.asm.annotations.cls;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface InnerClass {
    ClassRef name();

    ClassRef outerName() default @ClassRef();

    String innerName() default "";

    int access();
}
