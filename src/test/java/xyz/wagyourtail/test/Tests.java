package xyz.wagyourtail.test;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import xyz.wagyourtail.asm.annotations.AnnotationASM;
import xyz.wagyourtail.asm.annotations.MethodASM;
import xyz.wagyourtail.asm.annotations.md.InsnNode;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    @MethodASM(
        addVisibleAnnotations = {
            @AnnotationASM(
                owner = @ClassRef(value = MethodASM.class),
                arrayValues = {
                    @AnnotationASM.ArrayValue(
                        key = "code",
                        value = {
                            @AnnotationASM.Value(
                                annotationValue = @AnnotationASM(
                                    owner = @ClassRef(value = InsnNode.class),
                                    values = {
                                        @AnnotationASM.KeyValue(
                                            key = "ldc",
                                            value = @AnnotationASM.Value(
                                                annotationValue = @AnnotationASM(
                                                    owner = @ClassRef(value = InsnNode.LDCValue.class),
                                                    values = {
                                                        @AnnotationASM.KeyValue(
                                                            key = "stringValue",
                                                            value = @AnnotationASM.Value(stringValue = "0")
                                                        )
                                                    }
                                                )
                                            )
                                        ),
                                    }
                                )
                            ),
                            @AnnotationASM.Value(
                                annotationValue = @AnnotationASM(
                                    owner = @ClassRef(value = InsnNode.class),
                                    values = {
                                        @AnnotationASM.KeyValue(
                                            key = "opcode",
                                            value = @AnnotationASM.Value(intValue = Opcodes.ARETURN)
                                        )
                                    }
                                )
                            )
                        }
                    )
                }
            )
        },
        code = {
            @InsnNode(ldc = @InsnNode.LDCValue(stringValue = "1")),
            @InsnNode(opcode = Opcodes.ARETURN)
        }
    )
    public static String changeContents() {
        return "2";
    }

    @Test
    public void testChangeContents() {
        assertEquals("1", changeContents());
    }

}
