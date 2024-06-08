package xyz.wagyourtail.asm.parsers;

import org.objectweb.asm.TypePath;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.TypeAnnotationNode;

public class TypeAnnotationParser {

    public static TypeAnnotationNode parseTypeAnnotationASM(AnnotationNode annotation) {
        Integer typeRef = null;
        String typePath = null;
        AnnotationNode annotationNode = null;
        for (int i = 0; i < annotation.values.size(); i += 2) {
            String key = (String) annotation.values.get(i);
            Object value = annotation.values.get(i + 1);
            switch (key) {
                case "typeRef":
                    typeRef = (Integer) value;
                    break;
                case "typePath":
                    typePath = (String) value;
                    break;
                case "annotation":
                    annotationNode = AnnotationParser.parseAnnotationASM((AnnotationNode) value);
                    break;
            }
        }
        if (typeRef == null) {
            throw new IllegalArgumentException("TypeAnnotation annotation must have a typeRef");
        }
        TypeAnnotationNode node = new TypeAnnotationNode(typeRef, TypePath.fromString(typePath), annotationNode.desc);
        annotationNode.accept(node);
        return node;
    }

}
