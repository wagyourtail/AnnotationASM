package xyz.wagyourtail.asm.annotations;

import xyz.wagyourtail.asm.annotations.ref.ClassRef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface FieldASM {

    /**
     * if the field access should be changed.
     */
    int access() default 0;

    /**
     * if the field name should be changed.
     */
    String name() default "";

    /**
     * if the field descriptor should be changed.
     */
    ClassRef desc() default @ClassRef();

    /**
     * if the field signature should be changed.
     */
    String signature() default "";

    /**
     * if the field const value should be changed.
     * use empty @Value() to set to null.
     */
    Value value() default @Value();

    /**
     * if visible annotations should be added.
     */
    AnnotationASM[] addVisibleAnnotations() default {};

    /**
     * if invisible annotations should be added.
     */
    AnnotationASM[] addInvisibleAnnotations() default {};

    /**
     * if visible annotations should be removed.
     */
    ClassRef[] removeVisibleAnnotations() default {};

    /**
     * if invisible annotations should be removed.
     */
    ClassRef[] removeInvisibleAnnotations() default {};

    /**
     * if visible type annotations should be added.
     */
    AnnotationASM[] addVisibleTypeAnnotations() default {};

    /**
     * if invisible type annotations should be added.
     */
    AnnotationASM[] addInvisibleTypeAnnotations() default {};

    /**
     * if visible type annotations should be removed.
     */
    ClassRef[] removeVisibleTypeAnnotations() default {};

    /**
     * if invisible type annotations should be removed.
     */
    ClassRef[] removeInvisibleTypeAnnotations() default {};


    @interface Value {
        String stringValue() default "";

        int intValue() default 0;

        long longValue() default 0;

        float floatValue() default 0;

        double doubleValue() default 0;

        boolean booleanValue() default false;
    }
}
