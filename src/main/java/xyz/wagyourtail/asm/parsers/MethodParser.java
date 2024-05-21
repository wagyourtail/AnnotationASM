package xyz.wagyourtail.asm.parsers;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class MethodParser {

    public static void parseMethodASM(AnnotationNode annotation, MethodNode fieldNode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static MethodNode parseMethod(AnnotationNode annotation) {
        MethodNode mn = new MethodNode(-1, null, null, null, null);
        parseMethodASM(annotation, mn);
        if (mn.access == -1) {
            throw new IllegalArgumentException("Method annotation must have an access");
        }
        if (mn.name == null) {
            throw new IllegalArgumentException("Method annotation must have a name");
        }
        if (mn.desc == null) {
            throw new IllegalArgumentException("Method annotation must have a desc");
        }
        return mn;
    }

}
