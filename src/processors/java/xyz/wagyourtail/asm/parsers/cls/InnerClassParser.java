package xyz.wagyourtail.asm.parsers.cls;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.InnerClassNode;
import xyz.wagyourtail.asm.parsers.ref.ClassRefParser;

public class InnerClassParser {
    public static InnerClassNode parseInnerClassASM(AnnotationNode innerClass) {
        Type name = null;
        Type outerName = null;
        String innerName = null;
        Integer access = null;
        for (int i = 0; i < innerClass.values.size(); i += 2) {
            String key = (String) innerClass.values.get(i);
            Object value = innerClass.values.get(i + 1);
            switch (key) {
                case "name":
                    name = ClassRefParser.parseClassRef((AnnotationNode) value);
                    break;
                case "outerName":
                    outerName = ClassRefParser.parseClassRef((AnnotationNode) value);
                    break;
                case "innerName":
                    innerName = (String) value;
                    break;
                case "access":
                    access = (Integer) value;
                    break;
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("InnerClass annotation must have a name");
        }
        if (access == null) {
            throw new IllegalArgumentException("InnerClass annotation must have an access");
        }
        return new InnerClassNode(name.getInternalName(), outerName == null ? null : outerName.getInternalName(), innerName, access);
    }
}
