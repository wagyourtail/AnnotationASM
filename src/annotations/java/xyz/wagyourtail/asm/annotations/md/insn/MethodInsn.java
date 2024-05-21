package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.ref.MethodRef;

public @interface MethodInsn {
    int opcode();

    MethodRef.Qualified method();

    boolean isInterface();
}
