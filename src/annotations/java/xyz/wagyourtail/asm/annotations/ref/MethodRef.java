package xyz.wagyourtail.asm.annotations.ref;

public @interface MethodRef {

    String name();

    /**
     * The method descriptor to use, if present desc is ignored.
     * @return
     */
    String descString() default "";

    /**
     * The method descriptor to use.
     * @return
     */
    Desc desc() default @Desc(returnType = @ClassRef(ClassRef.class), args = {});

    @interface Desc {
        ClassRef returnType();
        ClassRef[] args();
    }

    @interface Qualified {
        ClassRef owner();
        MethodRef method();
    }

}
