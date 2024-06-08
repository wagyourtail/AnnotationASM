package xyz.wagyourtail.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import xyz.wagyourtail.asm.annotations.ClassASM;
import xyz.wagyourtail.asm.annotations.FieldASM;
import xyz.wagyourtail.asm.annotations.MethodASM;
import xyz.wagyourtail.asm.parsers.AnnotationParser;
import xyz.wagyourtail.asm.parsers.FieldParser;
import xyz.wagyourtail.asm.parsers.MethodParser;
import xyz.wagyourtail.asm.parsers.TypeAnnotationParser;
import xyz.wagyourtail.asm.parsers.cls.InnerClassParser;
import xyz.wagyourtail.asm.parsers.cls.RecordComponentParser;
import xyz.wagyourtail.asm.parsers.ref.ClassRefParser;
import xyz.wagyourtail.asm.parsers.ref.FieldRefParser;
import xyz.wagyourtail.asm.parsers.ref.MethodRefParser;

import java.util.Iterator;
import java.util.List;

public class ClassProcessor {

    private static void processClassASM(AnnotationNode annotation, ClassNode classNode) {
        for (int i = 0; i < annotation.values.size(); i += 2) {
            String key = (String) annotation.values.get(i);
            Object value = annotation.values.get(i + 1);
            switch (key) {
                case "version":
                    classNode.version = (Integer) value;
                    break;
                case "access":
                    classNode.access = (Integer) value;
                    break;
                case "name":
                    classNode.name = (String) value;
                    break;
                case "signature":
                    classNode.signature = (String) value;
                    break;
                case "superName":
                    classNode.superName = ClassRefParser.parseClassRef((AnnotationNode) value).getInternalName();
                    break;
                case "addInterfaces":
                    for (AnnotationNode interfaceNode : (List<AnnotationNode>) value) {
                        classNode.interfaces.add(ClassRefParser.parseClassRef(interfaceNode).getInternalName());
                    }
                    break;
                case "removeInterfaces":
                    for (AnnotationNode interfaceNode : (List<AnnotationNode>) value) {
                        classNode.interfaces.remove(ClassRefParser.parseClassRef(interfaceNode).getInternalName());
                    }
                    break;
                case "outerClass":
                    classNode.outerClass = ClassRefParser.parseClassRef((AnnotationNode) value).getInternalName();
                    break;
                case "outerMethod":
                    MemberNameAndDesc member = MethodRefParser.parseMethodRef((AnnotationNode) value);
                    classNode.outerMethod = member.name;
                    classNode.outerMethodDesc = member.desc.getDescriptor();
                    break;
                case "addVisibleAnnotation":
                    classNode.visibleAnnotations.add(AnnotationParser.parseAnnotationASM((AnnotationNode) value));
                    break;
                case "addInvisibleAnnotation":
                    classNode.invisibleAnnotations.add(AnnotationParser.parseAnnotationASM((AnnotationNode) value));
                    break;
                case "removeVisibleAnnotation":
                    classNode.visibleAnnotations.removeIf(annotationNode -> annotationNode.desc.equals(ClassRefParser.parseClassRef((AnnotationNode) value).getDescriptor()));
                    break;
                case "removeInvisibleAnnotation":
                    classNode.invisibleAnnotations.removeIf(annotationNode -> annotationNode.desc.equals(ClassRefParser.parseClassRef((AnnotationNode) value).getDescriptor()));
                    break;
                case "addVisibleTypeAnnotation":
                    classNode.visibleTypeAnnotations.add(TypeAnnotationParser.parseTypeAnnotationASM((AnnotationNode) value));
                    break;
                case "addInvisibleTypeAnnotation":
                    classNode.invisibleTypeAnnotations.add(TypeAnnotationParser.parseTypeAnnotationASM((AnnotationNode) value));
                    break;
                case "removeVisibleTypeAnnotation":
                    classNode.visibleTypeAnnotations.removeIf(typeAnnotationNode -> typeAnnotationNode.desc.equals(ClassRefParser.parseClassRef((AnnotationNode) value).getDescriptor()));
                    break;
                case "removeInvisibleTypeAnnotation":
                    classNode.invisibleTypeAnnotations.removeIf(typeAnnotationNode -> typeAnnotationNode.desc.equals(ClassRefParser.parseClassRef((AnnotationNode) value).getDescriptor()));
                    break;
                case "addInnerClasses":
                    for (AnnotationNode innerClassNode : (List<AnnotationNode>) value) {
                        classNode.innerClasses.add(InnerClassParser.parseInnerClassASM(innerClassNode));
                    }
                    break;
                case "removeInnerClasses":
                    for (AnnotationNode innerClassNode : (List<AnnotationNode>) value) {
                        classNode.innerClasses.removeIf(innerClass -> innerClass.name.equals(ClassRefParser.parseClassRef(innerClassNode).getInternalName()));
                    }
                    break;
                case "nestHost":
                    classNode.nestHostClass = ClassRefParser.parseClassRef((AnnotationNode) value).getInternalName();
                    break;
                case "addNestMembers":
                    for (AnnotationNode nestMemberNode : (List<AnnotationNode>) value) {
                        classNode.nestMembers.add(ClassRefParser.parseClassRef(nestMemberNode).getInternalName());
                    }
                    break;
                case "removeNestMembers":
                    for (AnnotationNode nestMemberNode : (List<AnnotationNode>) value) {
                        classNode.nestMembers.remove(ClassRefParser.parseClassRef(nestMemberNode).getInternalName());
                    }
                    break;
                case "addPermittedSubclasses":
                    for (AnnotationNode permittedSubclassNode : (List<AnnotationNode>) value) {
                        classNode.permittedSubclasses.add(ClassRefParser.parseClassRef(permittedSubclassNode).getInternalName());
                    }
                    break;
                case "removePermittedSubclasses":
                    for (AnnotationNode permittedSubclassNode : (List<AnnotationNode>) value) {
                        classNode.permittedSubclasses.remove(ClassRefParser.parseClassRef(permittedSubclassNode).getInternalName());
                    }
                    break;
                case "addRecordComponents":
                    for (AnnotationNode recordComponentNode : (List<AnnotationNode>) value) {
                        classNode.recordComponents.add(RecordComponentParser.parseRecordComponent(recordComponentNode));
                    }
                    break;
                case "removeRecordComponents":
                    for (String recordComponentName : (List<String>) value) {
                        classNode.recordComponents.removeIf(recordComponent -> recordComponent.name.equals(recordComponentName));
                    }
                    break;
                case "addFields":
                    for (AnnotationNode fieldNode : (List<AnnotationNode>) value) {
                        classNode.fields.add(FieldParser.parseField(fieldNode));
                    }
                    break;
                case "removeFields":
                    for (AnnotationNode fieldNode : (List<AnnotationNode>) value) {
                        MemberNameAndDesc fmember = FieldRefParser.parseFieldRef(fieldNode);
                        classNode.fields.removeIf(field -> field.name.equals(fmember.name) && field.desc.equals(fmember.desc.getDescriptor()));
                    }
                    break;
                case "addMethods":
                    for (AnnotationNode methodNode : (List<AnnotationNode>) value) {
                        classNode.methods.add(MethodParser.parseMethod(methodNode));
                    }
                    break;
                case "removeMethods":
                    for (AnnotationNode methodNode : (List<AnnotationNode>) value) {
                        MemberNameAndDesc mmember = MethodRefParser.parseMethodRef(methodNode);
                        classNode.methods.removeIf(method -> method.name.equals(mmember.name) && method.desc.equals(mmember.desc.getDescriptor()));
                    }
                    break;
            }
        }
    }

    public static ClassNode process(ClassNode classNode) {
        if (classNode.invisibleAnnotations != null) {
            Iterator<AnnotationNode> iter = classNode.invisibleAnnotations.iterator();
            while (iter.hasNext()) {
                AnnotationNode annotation = iter.next();
                if (annotation.desc.equals(Type.getType(ClassASM.class).getDescriptor())) {
                    processClassASM(annotation, classNode);
                    iter.remove();
                    break;
                }
            }
        }
        if (classNode.methods != null) {
            for (MethodNode method : classNode.methods) {
                if (method.invisibleAnnotations != null) {
                    Iterator<AnnotationNode> iter = method.invisibleAnnotations.iterator();
                    while (iter.hasNext()) {
                        AnnotationNode annotation = iter.next();
                        if (annotation.desc.equals(Type.getType(MethodASM.class).getDescriptor())) {
                            MethodParser.parseMethodASM(annotation, method);
                            iter.remove();
                            break;
                        }
                    }
                }
            }
        }
        if (classNode.fields != null) {
            for (FieldNode field : classNode.fields) {
                if (field.invisibleAnnotations != null) {
                    Iterator<AnnotationNode> iter = field.invisibleAnnotations.iterator();
                    while (iter.hasNext()) {
                        AnnotationNode annotation = iter.next();
                        if (annotation.desc.equals(Type.getType(FieldASM.class).getDescriptor())) {
                            FieldParser.parseFieldASM(annotation, field);
                            iter.remove();
                            break;
                        }
                    }
                }
            }
        }
        return classNode;
    }
}
