package xyz.wagyourtail.asm.annotations.md.shared;

import xyz.wagyourtail.asm.annotations.md.insn.InvokeDynamicInsn;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface ConstantDynamic {
    String name();

    ClassRef desc();

    Handle bsm();

    InvokeDynamicInsn.BSMArg[] bsmArgs() default {};
}
