package xyz.wagyourtail.asm.annotations.md.insn;

public @interface TableSwitchInsn {
    int min();
    int max();
    String defaultLabel();
    String[] labels();
}
