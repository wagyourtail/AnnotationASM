package xyz.wagyourtail.asm.annotations;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

public @interface AnnotationASM {
    ClassRef owner() default @ClassRef(ClassRef.class);

    ArrayValue[] arrayValues() default {};

    KeyValue[] values() default {};

    @interface KeyValue {
        String key();
        Value value();
    }

    @interface ArrayValue {
        String name();

        /**
         * @see Value
         * @return
         */
        Value[] value();
    }

    @interface Value {
        //TODO: use this library to insert this method, it's valid in jvm, but not java
        // AnnotationValue annotationValue() default @AnnotationASM(owner = @ClassRef);

        ClassRef classRef() default @ClassRef(ClassRef.class);

        String stringValue() default "";

        int intValue() default 0;

        long longValue() default 0;

        float floatValue() default 0;

        double doubleValue() default 0;
    }

}
