package xyz.wagyourtail.asm.annotations.md;

import org.objectweb.asm.Opcodes;
import xyz.wagyourtail.asm.annotations.md.insn.*;
import xyz.wagyourtail.asm.annotations.md.shared.Handle;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;
import xyz.wagyourtail.asm.annotations.ref.FieldRef;
import xyz.wagyourtail.asm.annotations.ref.MethodRef;

public @interface InsnNode {

    int opcode() default 0;

    /**
     * visitFrame
     */
    FrameInsn frame() default @FrameInsn(type = Opcodes.F_SAME, numLocal = 0, numStack = 0);

    /**
     * visitIntInsn
     */
    IntInsn intVal() default @IntInsn(opcode = 0, value = 0);

    /**
     * visitVarInsn
     */
    IntInsn var() default @IntInsn(opcode = 0, value = 0);

    /**
     * visitTypeInsn
     */
    TypeInsn type() default @TypeInsn(
        opcode = 0,
        type = @ClassRef()
    );

    /**
     * visitFieldInsn
     */
    FieldInsn field() default @FieldInsn(
        opcode = 0,
        field = @FieldRef.Qualified(owner = @ClassRef(), field = @FieldRef(name = "", desc = @ClassRef()))
    );

    /**
     * visitMethodInsn
     */
    MethodInsn method() default @MethodInsn(
        opcode = 0,
        method = @MethodRef.Qualified(owner = @ClassRef(), method = @MethodRef(name = "", desc = @MethodRef.Desc(returnType = @ClassRef(), args = {}))),
        isInterface = false
    );

    /**
     * visitInvokeDynamicInsn
     */
    InvokeDynamicInsn invokeDynamic() default @InvokeDynamicInsn(
        targetName = @MethodRef(name = "", desc = @MethodRef.Desc(returnType = @ClassRef(), args = {})),
        bsm = @Handle(
            tag = 0,
            method = @MethodRef.Qualified(owner = @ClassRef(), method = @MethodRef(name = "", desc = @MethodRef.Desc(returnType = @ClassRef(), args = {})))
        )
    );

    /**
     * visitJumpInsn
     */
    JumpInsn jump() default @JumpInsn(
        opcode = 0,
        label = ""
    );

    /**
     * visitLabel
     */
    String label() default "";

    /**
     * visitLdcInsn
     */
    LDCValue ldc() default @LDCValue;

    /**
     * visitIincInsn
     */
    IIncInsn iinc() default @IIncInsn(
        varIndex = 0,
        increment = 0
    );

    /**
     * visitTableSwitchInsn
     */
    TableSwitchInsn tableSwitch() default @TableSwitchInsn(
        min = 0,
        max = 0,
        defaultLabel = "",
        labels = {}
    );

    /**
     * visitLookupSwitchInsn
     */
    LookupSwitchInsn lookupSwitch() default @LookupSwitchInsn(
        defaultLabel = ""
    );

    /**
     * visitMultiANewArrayInsn
     */
    MultiANewArrayInsn multiANewArray() default @MultiANewArrayInsn(
        type = @ClassRef(),
        dims = 0
    );

    /**
     * visitInsnAnnotation
     */
    InsnAnnotation annotation() default @InsnAnnotation(
        typeRef = 0,
        desc = @ClassRef()
    );

    /**
     * visitTryCatchBlock
     */
    TryCatchBlock tryCatchBlock() default @TryCatchBlock(
        start = "",
        end = "",
        handler = "",
        type = @ClassRef()
    );

    /**
     * visitLocalVariable
     */
    LocalVariable localVariable() default @LocalVariable(
        name = "",
        desc = @ClassRef(),
        signature = "",
        startLabel = "",
        endLabel = "",
        index = 0
    );

    /**
     * visitLocalVariableAnnotation
     */
    LocalVariableAnnotation localVariableAnnotation() default @LocalVariableAnnotation(
        typeRef = 0,
        startLabels = {},
        endLabels = {},
        indexes = {},
        desc = @ClassRef()
    );

    /**
     * visitLineNumber
     */
    LineNumber lineNumber() default @LineNumber(
        line = 0,
        label = ""
    );

    /**
     * visitMaxs
     */
    Maxs maxs() default @Maxs(
        maxStack = 0,
        maxLocals = 0
    );

    @interface LDCValue {
        int intValue() default 0;
        float floatValue() default 0;
        long longValue() default 0;
        double doubleValue() default 0;
        String stringValue() default "";
        ClassRef classValue() default @ClassRef();
        Handle handleValue() default @Handle(tag = 0);
    }

}
