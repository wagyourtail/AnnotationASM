package xyz.wagyourtail.asm.parsers.ref;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

public class ClassRefParser {
    public static Type parseClassRef(AnnotationNode classRef) {
        Type valueType = null;
        for (int i = 0; i < classRef.values.size(); i += 2) {
            String key = (String) classRef.values.get(i);
            Object value = classRef.values.get(i + 1);
            switch (key) {
                case "value":
                    valueType = (Type) value;
                    break;
                case "stringValue":
                    return Type.getType((String) value);
            }
        }
        if (valueType == null) {
            throw new IllegalArgumentException("ClassRef annotation must have a value or stringValue");
        }
        return valueType;
    }
}
