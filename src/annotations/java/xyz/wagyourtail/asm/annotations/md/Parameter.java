package xyz.wagyourtail.asm.annotations.md;

public @interface Parameter {
    /**
     * default value to insert to the end
     */
    int index() default -1;

    String name();

    int access();

}
