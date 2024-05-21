package xyz.wagyourtail.asm.annotations.ref;

public @interface ClassRef {
    /**
     * The class reference to use.
     */
    Class<?> value();

    /**
     * The class reference to use, if present value is ignored.
     */
    String stringValue() default "";

}
