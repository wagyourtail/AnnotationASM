package xyz.wagyourtail.asm.parsers;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldNode;

public class FieldParser {

    public static void parseFieldASM(AnnotationNode annotation, FieldNode fieldNode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static FieldNode parseField(AnnotationNode annotation) {
        FieldNode fn = new FieldNode(-1, null, null, null, null);
        parseFieldASM(annotation, fn);
        if (fn.access == -1) {
            throw new IllegalArgumentException("Field annotation must have an access");
        }
        if (fn.name == null) {
            throw new IllegalArgumentException("Field annotation must have a name");
        }
        if (fn.desc == null) {
            throw new IllegalArgumentException("Field annotation must have a desc");
        }
        return fn;
    }

}
