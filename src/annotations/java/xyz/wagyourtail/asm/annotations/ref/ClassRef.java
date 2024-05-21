package xyz.wagyourtail.asm.annotations.ref;

public @interface ClassRef {
    /**
     * The class reference to use.
     */
    Class<?> value() default ClassRef.class;

    /**
     * The class reference to use, if present value is ignored.
     * as a field descriptor
     */
    String stringValue() default "";

}
