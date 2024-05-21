package xyz.wagyourtail.asm.annotations;

import xyz.wagyourtail.asm.annotations.cls.InnerClass;
import xyz.wagyourtail.asm.annotations.cls.RecordComponent;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;
import xyz.wagyourtail.asm.annotations.ref.FieldRef;
import xyz.wagyourtail.asm.annotations.ref.MethodRef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ClassASM {
    /**
     * if the class version should be changed.
     */
    int version() default 0;

    /**
     * if the class access should be changed.
     */
    int access() default -1;

    /**
     * if the class name should
     */
    String name() default "";

    /**
     * if the class signature should
     */
    String signature() default "";

    /**
     * if the class superName should
     */
    ClassRef superName() default @ClassRef();

    /**
     * interfaces to add to the class.
     */
    ClassRef[] addInterfaces() default {};

    /**
     * interfaces to remove from the class.
     */
    ClassRef[] removeInterfaces() default {};

    /**
     * if the class outerClass should be changed.
     */
    ClassRef outerClass() default @ClassRef();

    /**
     * if the outerMethod should be changed.
     */
    MethodRef outerMethod() default @MethodRef(name = "");

    /**
     * if any visible annotations should be added.
     */
    AnnotationASM[] addVisibleAnnotations() default {};

    /**
     * if any visible annotations should be removed.
     */
    ClassRef[] removeVisibleAnnotations() default {};

    /**
     * if any invisible annotations should be added.
     */
    AnnotationASM[] addInvisibleAnnotations() default {};

    /**
     * if any invisible annotations should be removed.
     */
    ClassRef[] removeInvisibleAnnotations() default {};

    /**
     * if any visible type annotations should be added.
     */
    TypeAnnotationASM[] addVisibleTypeAnnotations() default {};

    /**
     * if any visible type annotations should be removed.
     */
    ClassRef[] removeVisibleTypeAnnotations() default {};

    /**
     * if any invisible type annotations should be added.
     */
    TypeAnnotationASM[] addInvisibleTypeAnnotations() default {};

    /**
     * if any invisible type annotations should be removed.
     */
    ClassRef[] removeInvisibleTypeAnnotations() default {};

    /**
     * if any inner classes should be added.
     */
    InnerClass[] addInnerClasses() default {};

    /**
     * if any inner classes should be removed.
     */
    ClassRef[] removeInnerClasses() default {};

    /**
     * if the nestHost should be changed.
     */
    ClassRef nestHost() default @ClassRef();

    /**
     * if any nestMembers should be added.
     */
    ClassRef[] addNestMembers() default {};

    /**
     * if any nestMembers should be removed.
     */
    ClassRef[] removeNestMembers() default {};

    /**
     * if any permittedSubclasses should be added.
     */
    ClassRef[] addPermittedSubclasses() default {};

    /**
     * if any permittedSubclasses should be removed.
     */
    ClassRef[] removePermittedSubclasses() default {};

    /**
     * if any record components should be added.
     */
    RecordComponent[] addRecordComponents() default {};

    /**
     * if any record components should be removed.
     */
    String[] removeRecordComponents() default {};

    /**
     * if any fields should be added.
     */
    FieldASM[] addFields() default {};

    /**
     * if any fields should be removed.
     */
    FieldRef[] removeFields() default {};

    /**
     * if any methods should be added.
     */
    MethodASM[] addMethods() default {};

    /**
     * if any methods should be removed.
     */
    MethodRef[] removeMethods() default {};

}
