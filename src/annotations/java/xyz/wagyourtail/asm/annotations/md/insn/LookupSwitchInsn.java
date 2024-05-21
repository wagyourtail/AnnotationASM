package xyz.wagyourtail.asm.annotations.md.insn;

public @interface LookupSwitchInsn {
    String defaultLabel();
    Pair[] pairs() default {};

    @interface Pair {
        int key();
        String label();
    }
}
