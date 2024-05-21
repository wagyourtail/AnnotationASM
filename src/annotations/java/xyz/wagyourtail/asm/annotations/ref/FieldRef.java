package xyz.wagyourtail.asm.annotations.ref;

public @interface FieldRef {

    String name();

    /**
     * The field descriptor to use
     */
    ClassRef desc();

    /**
     * The field descriptor to use, if present desc is ignored.
     */
    String descString() default "";

    @interface Qualified {
        ClassRef owner();
        FieldRef field();
    }
}
