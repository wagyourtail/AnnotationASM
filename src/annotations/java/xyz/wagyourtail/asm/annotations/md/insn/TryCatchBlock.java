package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface TryCatchBlock {
    String start();
    String end();
    String handler();
    ClassRef type();
}
