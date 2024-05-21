package xyz.wagyourtail.asm.annotations.md.insn;

public @interface JumpInsn {
    int opcode();
    String label();
}
