package xyz.wagyourtail.asm.annotations.ref;

public @interface EnumRef {

    /**
     * The class reference to use.
     */
    ClassRef owner();

    /**
     * The name of the enum constant.
     */
    String name();

}
