package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface TypeInsn {
    int opcode();
    ClassRef type();
}
