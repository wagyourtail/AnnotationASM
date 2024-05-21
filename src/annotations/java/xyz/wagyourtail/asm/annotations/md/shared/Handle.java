package xyz.wagyourtail.asm.annotations.md.shared;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;
import xyz.wagyourtail.asm.annotations.ref.FieldRef;
import xyz.wagyourtail.asm.annotations.ref.MethodRef;

public @interface Handle {
    int tag();

    MethodRef.Qualified method() default @MethodRef.Qualified(owner = @ClassRef(value = ClassRef.class), method = @MethodRef(name = "", desc = @MethodRef.Desc(returnType = @ClassRef(value = ClassRef.class), args = {})));

    FieldRef.Qualified field() default @FieldRef.Qualified(owner = @ClassRef(value = ClassRef.class), field = @FieldRef(name = "", desc = @ClassRef(value = ClassRef.class)));

    boolean isInterface() default false;
}
