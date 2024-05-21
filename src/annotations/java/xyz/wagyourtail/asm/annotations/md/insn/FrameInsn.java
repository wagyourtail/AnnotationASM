package xyz.wagyourtail.asm.annotations.md.insn;

import org.objectweb.asm.Opcodes;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;

/**
 * visitInvokeDynamicInsn
 */


public @interface FrameInsn {
    int type();

    int numLocal();

    FrameValue[] local() default {};

    int numStack();

    FrameValue[] stack() default {};

    @interface FrameValue {

        /**
         * if a primitive, set primitive opcode:
         * Opcodes.TOP,
         * Opcodes.INTEGER,
         * Opcodes.FLOAT,
         * Opcodes.LONG,
         * Opcodes.DOUBLE,
         * Opcodes.NULL,
         * Opcodes.UNINITIALIZED_THIS
         */
        int primitive();

        /**
         * if a object
         */
        ClassRef object();
    }
}
