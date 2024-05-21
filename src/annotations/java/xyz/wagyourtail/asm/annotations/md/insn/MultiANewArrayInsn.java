package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface MultiANewArrayInsn {
    ClassRef type();
    int dims();
}
