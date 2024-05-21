package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface LocalVariable {
    String name();
    ClassRef desc();
    String signature() default "";
    String startLabel();
    String endLabel();
    int index();
}
