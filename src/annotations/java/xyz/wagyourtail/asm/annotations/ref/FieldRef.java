package xyz.wagyourtail.asm.annotations.ref;

public @interface FieldRef {

    String name();

    /**
     * The field descriptor to use
     */
    ClassRef desc();

    @interface Qualified {
        ClassRef owner();
        FieldRef field();
    }
}
