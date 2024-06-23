package xyz.wagyourtail.test;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import xyz.wagyourtail.asm.annotations.MethodASM;
import xyz.wagyourtail.asm.annotations.md.InsnNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    @MethodASM(
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
