package xyz.wagyourtail.test;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import xyz.wagyourtail.asm.annotations.AnnotationASM;
import xyz.wagyourtail.asm.annotations.MethodASM;
import xyz.wagyourtail.asm.annotations.md.InsnNode;
import xyz.wagyourtail.asm.annotations.ref.ClassRef;

import java.util.function.Function;

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

    public static void testLambda() {
        String a = "test";
        Function<String, String> test = (f) -> a + f;
        test.apply("test2");
    }

//    @Test
//    @MethodASM(
//        code = {
//            @InsnNode(ldc = @InsnNode.LDCValue(intValue = 1)),
//            @InsnNode(
//                tableSwitch = @TableSwitchInsn(
//                    min = 0,
//                    max = 1,
//                    defaultLabel = "eos0",
//                    labels = {
//                        "s0c0",
//                        "s1c1"
//                    }
//                )
//            ),
//            @InsnNode(label = "s0c0"),
//            @InsnNode(jump = @JumpInsn(opcode = Opcodes.GOTO, label = "eos0")),
//            @InsnNode(label = "eos0"),
//            @InsnNode(ldc = @InsnNode.LDCValue(intValue = 1)),
//            @InsnNode(
//                tableSwitch = @TableSwitchInsn(
//                    min = -1,
//                    max = 1,
//                    defaultLabel = "eof",
//                    labels = {
//                        "s1c0",
//                        "s1c1"
//                    }
//                )
//            ),
//            @InsnNode(label = "s0c0"),
//            @InsnNode(jump = @JumpInsn(opcode = Opcodes.GOTO, label = "eof")),
//            @InsnNode(label = "s0c1"),
//            @InsnNode(jump = @JumpInsn(opcode = Opcodes.GOTO, label = "eof")),
//            @InsnNode(label = "eof")
//        }
//    )
//    public void testVF() {
//
//    }

    @Test
    public void testChild() {
        TestParent test = new TestChild();
        assertEquals("parent", test.test());
    }

    public static class TestParent {
        public String test() {
            return "parent";
        }
    }

    public static class TestChild extends TestParent {

        @MethodASM(
            name = "test"
        )
        private String test2() {
            return "child";
        }

    }

}
