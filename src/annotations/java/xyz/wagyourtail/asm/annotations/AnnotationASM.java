package xyz.wagyourtail.asm.annotations;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;
import xyz.wagyourtail.asm.annotations.ref.EnumRef;
import xyz.wagyourtail.asm.annotations.ref.MethodRef;

public @interface AnnotationASM {
    ClassRef owner();

    ArrayValue[] arrayValues() default {};

    KeyValue[] values() default {};

    @interface KeyValue {
        String key();

        @MethodASM(
            desc = @MethodRef.Desc(
                returnType = @ClassRef(Value.class), args = {}
            )
        )
        String value();
    }

    @interface ArrayValue {
        String key();

        @MethodASM(
            desc = @MethodRef.Desc(
                returnType = @ClassRef(Value[].class), args = {}
            )
        )
        String[] value();
    }

    @interface Value {
        AnnotationASM annotationValue() default @AnnotationASM(owner = @ClassRef);

        ClassRef classRef() default @ClassRef();

        EnumRef enumRef() default @EnumRef(owner = @ClassRef, name = "");

        String stringValue() default "";

        int intValue() default 0;

        long longValue() default 0;

        float floatValue() default 0;

        double doubleValue() default 0;

        boolean booleanValue() default false;
    }

}
