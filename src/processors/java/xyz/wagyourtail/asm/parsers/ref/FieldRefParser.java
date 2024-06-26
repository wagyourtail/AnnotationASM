package xyz.wagyourtail.asm.parsers.ref;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import xyz.wagyourtail.asm.MemberNameAndDesc;
import xyz.wagyourtail.asm.QualifiedMemberNameAndDesc;

import java.util.ArrayList;
import java.util.List;

public class FieldRefParser {

    public static MemberNameAndDesc parseFieldRef(AnnotationNode node) {
        String name = null;
        Type desc = null;
        Type descStr = null;
        for (int i = 0; i < node.values.size(); i += 2) {
            String key = (String) node.values.get(i);
            Object value = node.values.get(i + 1);
            switch (key) {
                case "name":
                    name = (String) value;
                    break;
                case "desc":
                    desc = ClassRefParser.parseClassRef((AnnotationNode) value);
                    break;
                case "descString":
                    descStr = Type.getMethodType((String) value);
                    break;
            }
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("FieldRef annotation must have a name");
        }
        if (descStr != null) {
            return new MemberNameAndDesc(name, descStr);
        }
        if (desc == null) {
            throw new IllegalArgumentException("FieldRef annotation must have a desc or descString");
        }
        return new MemberNameAndDesc(name, desc);
    }

    public static QualifiedMemberNameAndDesc parseQualifiedFieldRef(AnnotationNode node) {
        Type owner = null;
        MemberNameAndDesc field = null;
        for (int i = 0; i < node.values.size(); i += 2) {
            String key = (String) node.values.get(i);
            Object value = node.values.get(i + 1);
            switch (key) {
                case "owner":
                    owner = ClassRefParser.parseClassRef((AnnotationNode) value);
                    break;
                case "field":
                    field = parseFieldRef((AnnotationNode) value);
                    break;
            }
        }
        if (owner == null) {
            throw new IllegalArgumentException("QualifiedFieldRef annotation must have an owner");
        }
        if (field == null) {
            throw new IllegalArgumentException("QualifiedFieldRef annotation must have a field");
        }
        return new QualifiedMemberNameAndDesc(owner, field.name, field.desc);
    }

}
