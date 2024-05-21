package xyz.wagyourtail.asm.parsers.cls;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.RecordComponentNode;
import org.objectweb.asm.tree.TypeAnnotationNode;
import xyz.wagyourtail.asm.parsers.AnnotationParser;
import xyz.wagyourtail.asm.parsers.TypeAnnotationParser;
import xyz.wagyourtail.asm.parsers.ref.ClassRefParser;

import java.util.ArrayList;
import java.util.List;

public class RecordComponentParser {

    public static RecordComponentNode parseRecordComponent(AnnotationNode annotation) {
        String name = null;
        Type desc = null;
        String signature = null;
        List<AnnotationNode> visibleAnnotations = new ArrayList<>();
        List<AnnotationNode> invisibleAnnotations = new ArrayList<>();
        List<TypeAnnotationNode> visibleTypeAnnotations = new ArrayList<>();
        List<TypeAnnotationNode> invisibleTypeAnnotations = new ArrayList<>();
        for (int i = 0; i < annotation.values.size(); i += 2) {
            String key = (String) annotation.values.get(i);
            Object value = annotation.values.get(i + 1);
            switch (key) {
                case "name":
                    name = (String) value;
                    break;
                case "desc":
                    desc = ClassRefParser.parseClassRef((AnnotationNode) value);
                    break;
                case "signature":
                    signature = (String) value;
                    break;
                case "visibleAnnotations":
                    for (AnnotationNode visibleAnnotation : (List<AnnotationNode>) value) {
                        visibleAnnotations.add(AnnotationParser.parseAnnotationASM(visibleAnnotation));
                    }
                    break;
                case "invisibleAnnotations":
                    for (AnnotationNode invisibleAnnotation : (List<AnnotationNode>) value) {
                        invisibleAnnotations.add(AnnotationParser.parseAnnotationASM(invisibleAnnotation));
                    }
                    break;
                case "visibleTypeAnnotations":
                    for (AnnotationNode visibleTypeAnnotation : (List<AnnotationNode>) value) {
                        visibleTypeAnnotations.add(TypeAnnotationParser.parseTypeAnnotationASM(visibleTypeAnnotation));
                    }
                    break;
                case "invisibleTypeAnnotations":
                    for (AnnotationNode invisibleTypeAnnotation : (List<AnnotationNode>) value) {
                        invisibleTypeAnnotations.add(TypeAnnotationParser.parseTypeAnnotationASM(invisibleTypeAnnotation));
                    }
                    break;
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("RecordComponent annotation must have a name");
        }
        if (desc == null) {
            throw new IllegalArgumentException("RecordComponent annotation must have a desc");
        }
        RecordComponentNode node = new RecordComponentNode(name, desc.getDescriptor(), signature);
        node.visibleAnnotations = visibleAnnotations;
        node.invisibleAnnotations = invisibleAnnotations;
        node.visibleTypeAnnotations = visibleTypeAnnotations;
        node.invisibleTypeAnnotations = invisibleTypeAnnotations;
        return node;
    }

}
