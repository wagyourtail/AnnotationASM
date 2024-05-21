package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.ref.FieldRef;

public @interface FieldInsn {
    int opcode();
    FieldRef.Qualified field();
}
