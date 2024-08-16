package xyz.wagyourtail.asm.annotations.md.insn;

import xyz.wagyourtail.asm.annotations.AnnotationASM;
import xyz.wagyourtail.asm.annotations.MethodASM;
import xyz.wagyourtail.asm.annotations.md.shared.ConstantDynamic;
import xyz.wagyourtail.asm.annotations.md.shared.Handle;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;
import xyz.wagyourtail.asm.annotations.ref.MethodRef;

public @interface InvokeDynamicInsn {

    MethodRef targetName();

    Handle bsm();

    BSMArg[] bsmArgs() default {};

    @interface BSMArg {
        String stringValue() default "";

        int intValue() default 0;

        long longValue() default 0;

        float floatValue() default 0;

        double doubleValue() default 0;

        ClassRef classRef() default @ClassRef();

        MethodRef.Desc methodDesc() default @MethodRef.Desc(returnType = @ClassRef(), args = {});

        Handle handle() default @Handle(tag = 0);

        @MethodASM(
            desc = @MethodRef.Desc(
                returnType = @ClassRef(ConstantDynamic.class), args = {}
            ),
            annotationDefault = @AnnotationASM.Value(
                annotationValue = @AnnotationASM(
                    owner = @ClassRef(ConstantDynamic.class)
                )
            )
        )
        String constantDynamic();
    }
}
