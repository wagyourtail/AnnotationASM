package xyz.wagyourtail.asm.annotations.cls;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface InnerClass {
    String name();

    ClassRef outerName() default @ClassRef(ClassRef.class);

    String innerName() default "";

    int access();
}
