package xyz.wagyourtail.asm.parsers.ref;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

public class EnumRefParser {

    public static String[] parseEnumRef(AnnotationNode enumRef) {
        Type owner = null;
        String name = null;

        for (int i = 0; i < enumRef.values.size(); i += 2) {
            String key = (String) enumRef.values.get(i);
            Object value = enumRef.values.get(i + 1);
            switch (key) {
                case "owner":
                    owner = ClassRefParser.parseClassRef((AnnotationNode) value);
                    break;
                case "name":
                    name = (String) value;
                    break;
            }
        }

        if (owner == null || name == null) {
            throw new IllegalArgumentException("EnumRef annotation must have an owner and name");
        }
        return new String[] { owner.getDescriptor(), name };
    }

}
