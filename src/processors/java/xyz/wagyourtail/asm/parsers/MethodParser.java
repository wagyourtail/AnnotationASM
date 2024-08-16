package xyz.wagyourtail.asm.parsers;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import xyz.wagyourtail.asm.MemberNameAndDesc;
import xyz.wagyourtail.asm.QualifiedMemberNameAndDesc;
import xyz.wagyourtail.asm.parsers.ref.ClassRefParser;
import xyz.wagyourtail.asm.parsers.ref.FieldRefParser;
import xyz.wagyourtail.asm.parsers.ref.MethodRefParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodParser {

    public static void parseMethodASM(AnnotationNode annotation, MethodNode methodNode) {
        for (int i = 0; i < annotation.values.size(); i += 2) {
            String key = (String) annotation.values.get(i);
            Object value = annotation.values.get(i + 1);
            switch (key) {
                case "access":
                    methodNode.access = (Integer) value;
                    break;
                case "name":
                    methodNode.name = (String) value;
                    break;
                case "desc":
                    methodNode.desc = MethodRefParser.parseMethodDesc((AnnotationNode) value).getDescriptor();
                    break;
                case "signature":
                    methodNode.signature = (String) value;
                    break;
                case "addExceptions":
                    if (methodNode.exceptions == null) {
                        methodNode.exceptions = new ArrayList<>();
                    }
                    for (AnnotationNode exception : (List<AnnotationNode>) value) {
                        methodNode.exceptions.add(ClassRefParser.parseClassRef(exception).getInternalName());
                    }
                    break;
                case "removeExceptions":
                    if (methodNode.exceptions != null) {
                        for (AnnotationNode exception : (List<AnnotationNode>) value) {
                            methodNode.exceptions.remove(ClassRefParser.parseClassRef(exception).getInternalName());
                        }
                    }
                    break;
                case "parameters":
                    throw new UnsupportedOperationException("Parameters not supported yet");
                case "addVisibleAnnotations":
                    if (methodNode.visibleAnnotations == null) {
                        methodNode.visibleAnnotations = new ArrayList<>();
                    }
                    for (AnnotationNode visibleAnnotation : (List<AnnotationNode>) value) {
                        methodNode.visibleAnnotations.add(AnnotationParser.parseAnnotationASM(visibleAnnotation));
                    }
                    break;
                case "addInvisibleAnnotations":
                    if (methodNode.invisibleAnnotations == null) {
                        methodNode.invisibleAnnotations = new ArrayList<>();
                    }
                    for (AnnotationNode invisibleAnnotation : (List<AnnotationNode>) value) {
                        methodNode.invisibleAnnotations.add(AnnotationParser.parseAnnotationASM(invisibleAnnotation));
                    }
                    break;
                case "addVisibleTypeAnnotations":
                    if (methodNode.visibleTypeAnnotations == null) {
                        methodNode.visibleTypeAnnotations = new ArrayList<>();
                    }
                    for (AnnotationNode visibleTypeAnnotation : (List<AnnotationNode>) value) {
                        methodNode.visibleTypeAnnotations.add(TypeAnnotationParser.parseTypeAnnotationASM(visibleTypeAnnotation));
                    }
                    break;
                case "addInvisibleTypeAnnotations":
                    if (methodNode.invisibleTypeAnnotations == null) {
                        methodNode.invisibleTypeAnnotations = new ArrayList<>();
                    }
                    for (AnnotationNode invisibleTypeAnnotation : (List<AnnotationNode>) value) {
                        methodNode.invisibleTypeAnnotations.add(TypeAnnotationParser.parseTypeAnnotationASM(invisibleTypeAnnotation));
                    }
                    break;
                case "removeVisibleAnnotations":
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode visibleAnnotation : (List<AnnotationNode>) value) {
                            methodNode.visibleAnnotations.removeIf(an -> an.desc.equals(ClassRefParser.parseClassRef(visibleAnnotation).getDescriptor()));
                        }
                    }
                    break;
                case "removeInvisibleAnnotations":
                    if (methodNode.invisibleAnnotations != null) {
                        for (AnnotationNode invisibleAnnotation : (List<AnnotationNode>) value) {
                            methodNode.invisibleAnnotations.removeIf(an -> an.desc.equals(ClassRefParser.parseClassRef(invisibleAnnotation).getDescriptor()));
                        }
                    }
                    break;
                case "removeVisibleTypeAnnotations":
                    if (methodNode.visibleTypeAnnotations != null) {
                        for (AnnotationNode visibleTypeAnnotation : (List<AnnotationNode>) value) {
                            methodNode.visibleTypeAnnotations.removeIf(an -> an.desc.equals(ClassRefParser.parseClassRef(visibleTypeAnnotation).getDescriptor()));
                        }
                    }
                    break;
                case "removeInvisibleTypeAnnotations":
                    if (methodNode.invisibleTypeAnnotations != null) {
                        for (AnnotationNode invisibleTypeAnnotation : (List<AnnotationNode>) value) {
                            methodNode.invisibleTypeAnnotations.removeIf(an -> an.desc.equals(ClassRefParser.parseClassRef(invisibleTypeAnnotation).getDescriptor()));
                        }
                    }
                    break;
                case "annotationDefault":
                    methodNode.annotationDefault = AnnotationParser.parseValue((AnnotationNode) value);
                    break;
                case "visibleAnnotableParameterCount":
                    methodNode.visibleAnnotableParameterCount = (Integer) value;
                    break;
                case "visibleParameterAnnotations":
                    throw new UnsupportedOperationException("Visible Parameter annotations not supported yet");
                case "invisibleAnnotableParameterCount":
                    methodNode.invisibleAnnotableParameterCount = (Integer) value;
                    break;
                case "invisibleParameterAnnotations":
                    throw new UnsupportedOperationException("Invisible Parameter annotations not supported yet");
                case "code":
                    parseCode((List<AnnotationNode>) value, methodNode);
            }
        }
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

    public static void parseCode(List<AnnotationNode> code, MethodNode methodNode) {
        methodNode.instructions = new InsnList();
        Map<String, LabelNode> labelMap = new HashMap<>();
        for (AnnotationNode insn : code) {
            if (insn.values.size() != 2) {
                throw new IllegalArgumentException("Invalid instruction, must only have 1 key and 1 value");
            }
            String key = (String) insn.values.get(0);
            Object value = insn.values.get(1);
            switch (key) {
                case "opcode":
                    methodNode.instructions.add(new InsnNode((Integer) value));
                    break;
                case "frame":
                    methodNode.instructions.add(parseFrame((AnnotationNode) value));
                    break;
                case "intVal":
                    methodNode.instructions.add(parseIntInsn((AnnotationNode) value));
                    break;
                case "var":
                    methodNode.instructions.add(parseVarInsn((AnnotationNode) value));
                    break;
                case "type":
                    methodNode.instructions.add(parseTypeInsn((AnnotationNode) value));
                    break;
                case "field":
                    methodNode.instructions.add(parseFieldInsn((AnnotationNode) value));
                    break;
                case "method":
                    methodNode.instructions.add(parseMethodInsn((AnnotationNode) value));
                    break;
                case "invokeDynamic":
                    methodNode.instructions.add(parseInvokeDynamicInsn((AnnotationNode) value));
                    break;
                case "jump":
                    methodNode.instructions.add(parseJumpInsn((AnnotationNode) value, labelMap));
                    break;
                case "label":
                    methodNode.instructions.add(labelMap.computeIfAbsent((String) value, k -> new LabelNode()));
                    break;
                case "ldc":
                    methodNode.instructions.add(parseLdcInsn((AnnotationNode) value));
                    break;
                case "iinc":
                    methodNode.instructions.add(parseIincInsn((AnnotationNode) value));
                    break;
                case "tableSwitch":
                    methodNode.instructions.add(parseTableSwitchInsn((AnnotationNode) value, labelMap));
                    break;
                case "lookupSwitch":
                    methodNode.instructions.add(parseLookupSwitchInsn((AnnotationNode) value, labelMap));
                    break;
                case "multiANewArray":
                    methodNode.instructions.add(parseMultiANewArrayInsn((AnnotationNode) value));
                    break;
                case "annotation":
                    throw new UnsupportedOperationException("Annotation not supported yet");
//                    methodNode.instructions.add(parseAnnotationInsn(insn));
                case "tryCatchBlock":
                    methodNode.tryCatchBlocks.add(parseTryCatchBlock((AnnotationNode) value, labelMap));
                    break;
                case "localVariable":
                    methodNode.localVariables.add(parseLocalVariable((AnnotationNode) value, labelMap));
                    break;
                case "localVariableAnnotation":
                    methodNode.visibleLocalVariableAnnotations.add(parseLocalVariableAnnotation((AnnotationNode) value));
                    break;
                case "lineNumber":
                    methodNode.instructions.add(parseLineNumber((AnnotationNode) value));
                    break;
                case "maxs":
                    parseMaxs((AnnotationNode) value, methodNode);
                    break;
            }
        }
    }

    public static FrameNode parseFrame(AnnotationNode frame) {
        Integer type = null;
        Integer nLocal = null;
        List<Object> local = null;
        Integer nStack = null;
        List<Object> stack = null;

        for (int i = 0; i < frame.values.size(); i += 2) {
            String key = (String) frame.values.get(i);
            Object value = frame.values.get(i + 1);
            switch (key) {
                case "type":
                    type = (Integer) value;
                    break;
                case "numLocal":
                    nLocal = (Integer) value;
                    break;
                case "local":
                    List<AnnotationNode> locals = (List<AnnotationNode>) value;
                    local = new ArrayList<>();
                    for (AnnotationNode localNode : locals) {
                        local.add(parseFrameValue(localNode));
                    }
                    break;
                case "nStack":
                    nStack = (Integer) value;
                    break;
                case "stack":
                    List<AnnotationNode> stacks = (List<AnnotationNode>) value;
                    stack = new ArrayList<>();
                    for (AnnotationNode stackNode : stacks) {
                        stack.add(parseFrameValue(stackNode));
                    }
                    break;
            }
        }
        if (type == null) {
            throw new IllegalArgumentException("Frame annotation must have a type");
        }
        if (nLocal == null) {
            throw new IllegalArgumentException("Frame annotation must have a numLocal");
        }
        if (local == null) {
            throw new IllegalArgumentException("Frame annotation must have a local");
        }
        return new FrameNode(type, nLocal, local.toArray(), nStack, stack.toArray());
    }

    private static Object parseFrameValue(AnnotationNode value) {
        if (value.values.size() != 2) {
            throw new IllegalArgumentException("Invalid frame value, must only have 1 key and 1 value");
        }
        String key = (String) value.values.get(0);
        Object val = value.values.get(1);
        switch (key) {
            case "primitive":
                return (Integer) val;
            case "object":
                return ClassRefParser.parseClassRef((AnnotationNode) val);
        }
        throw new IllegalArgumentException("Invalid frame value key: " + key);
    }

    public static IntInsnNode parseIntInsn(AnnotationNode insn) {
        Integer opcode = null;
        Integer value = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "opcode":
                    opcode = (Integer) val;
                    break;
                case "value":
                    value = (Integer) val;
                    break;
            }
        }
        if (opcode == null) {
            throw new IllegalArgumentException("IntInsn annotation must have an opcode");
        }
        if (value == null) {
            throw new IllegalArgumentException("IntInsn annotation must have a value");
        }
        return new IntInsnNode(opcode, value);
    }

    public static VarInsnNode parseVarInsn(AnnotationNode insn) {
        Integer opcode = null;
        Integer var = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "opcode":
                    opcode = (Integer) val;
                    break;
                case "value":
                    var = (Integer) val;
                    break;
            }
        }
        if (opcode == null) {
            throw new IllegalArgumentException("VarInsn annotation must have an opcode");
        }
        if (var == null) {
            throw new IllegalArgumentException("VarInsn annotation must have a var");
        }
        return new VarInsnNode(opcode, var);
    }

    public static TypeInsnNode parseTypeInsn(AnnotationNode insn) {
        Integer opcode = null;
        Type type = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "opcode":
                    opcode = (Integer) val;
                    break;
                case "type":
                    type = ClassRefParser.parseClassRef((AnnotationNode) val);
                    break;
            }
        }
        if (opcode == null) {
            throw new IllegalArgumentException("TypeInsn annotation must have an opcode");
        }
        if (type == null) {
            throw new IllegalArgumentException("TypeInsn annotation must have a type");
        }
        return new TypeInsnNode(opcode, type.getInternalName());
    }

    public static FieldInsnNode parseFieldInsn(AnnotationNode insn) {
        Integer opcode = null;
        QualifiedMemberNameAndDesc field = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "opcode":
                    opcode = (Integer) val;
                    break;
                case "field":
                    field = FieldRefParser.parseQualifiedFieldRef((AnnotationNode) val);
                    break;
            }
        }
        if (opcode == null) {
            throw new IllegalArgumentException("FieldInsn annotation must have an opcode");
        }
        if (field == null) {
            throw new IllegalArgumentException("FieldInsn annotation must have a field");
        }
        return new FieldInsnNode(opcode, field.owner.getInternalName(), field.name, field.desc.getDescriptor());
    }

    public static MethodInsnNode parseMethodInsn(AnnotationNode insn) {
        Integer opcode = null;
        QualifiedMemberNameAndDesc method = null;
        Boolean itf = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "opcode":
                    opcode = (Integer) val;
                    break;
                case "method":
                    method = MethodRefParser.parseQualifiedMethodRef((AnnotationNode) val);
                    break;
                case "isInterface":
                    itf = (Boolean) val;
                    break;
            }
        }
        if (opcode == null) {
            throw new IllegalArgumentException("MethodInsn annotation must have an opcode");
        }
        if (method == null) {
            throw new IllegalArgumentException("MethodInsn annotation must have a method");
        }
        if (itf == null) {
            throw new IllegalArgumentException("MethodInsn annotation must have an itf");
        }
        return new MethodInsnNode(opcode, method.owner.getInternalName(), method.name, method.desc.getDescriptor(), itf);
    }

    public static InvokeDynamicInsnNode parseInvokeDynamicInsn(AnnotationNode insn) {
        MemberNameAndDesc targetName = null;
        Handle bsm = null;
        List<Object> bsmArgs = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "targetName":
                    targetName = MethodRefParser.parseMethodRef((AnnotationNode) val);
                    break;
                case "bsm":
                    bsm = parseHandle((AnnotationNode) val);
                    break;
                case "bsmArgs":
                    List<AnnotationNode> args = (List<AnnotationNode>) val;
                    bsmArgs = new ArrayList<>();
                    for (AnnotationNode arg : args) {
                        bsmArgs.add(parseBSMArg(arg));
                    }
                    break;
            }
        }
        if (targetName == null) {
            throw new IllegalArgumentException("InvokeDynamicInsn annotation must have a targetName");
        }
        if (bsm == null) {
            throw new IllegalArgumentException("InvokeDynamicInsn annotation must have a bsm");
        }
        if (bsmArgs == null) {
            bsmArgs = new ArrayList<>();
        }
        return new InvokeDynamicInsnNode(targetName.name, targetName.desc.getDescriptor(), bsm, bsmArgs.toArray());
    }

    public static Object parseBSMArg(AnnotationNode bsmArg) {
        if (bsmArg.values.size() != 2) {
            throw new IllegalArgumentException("Invalid BSMArg, must only have 1 key and 1 value");
        }
        String key = (String) bsmArg.values.get(0);
        Object val = bsmArg.values.get(1);
        switch (key) {
            case "intValue":
                return (Integer) val;
            case "longValue":
                return (Long) val;
            case "floatValue":
                return (Float) val;
            case "doubleValue":
                return (Double) val;
            case "stringValue":
                return (String) val;
            case "classRef":
                return ClassRefParser.parseClassRef((AnnotationNode) val);
            case "methodRef":
                return MethodRefParser.parseMethodDesc((AnnotationNode) val);
            case "handle":
                return parseHandle((AnnotationNode) val);
            case "constantDynamic":
                return parseConstantDynamic((AnnotationNode) val);
        }
        throw new IllegalArgumentException("Invalid BSMArg key: " + key);
    }

    public static ConstantDynamic parseConstantDynamic(AnnotationNode constantDynamic) {
        String name = null;
        Type desc = null;
        Handle bsm = null;
        List<Object> bsmArgs = null;

        for (int i = 0; i < constantDynamic.values.size(); i += 2) {
            String key = (String) constantDynamic.values.get(i);
            Object val = constantDynamic.values.get(i + 1);
            switch (key) {
                case "name":
                    name = (String) val;
                    break;
                case "desc":
                    desc = ClassRefParser.parseClassRef((AnnotationNode) val);
                    break;
                case "bsm":
                    bsm = parseHandle((AnnotationNode) val);
                    break;
                case "bsmArgs":
                    List<AnnotationNode> args = (List<AnnotationNode>) val;
                    bsmArgs = new ArrayList<>();
                    for (AnnotationNode arg : args) {
                        bsmArgs.add(parseBSMArg(arg));
                    }
                    break;
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("ConstantDynamic annotation must have a name");
        }
        if (desc == null) {
            throw new IllegalArgumentException("ConstantDynamic annotation must have a desc");
        }
        if (bsm == null) {
            throw new IllegalArgumentException("ConstantDynamic annotation must have a bsm");
        }
        if (bsmArgs == null) {
            bsmArgs = new ArrayList<>();
        }
        return new ConstantDynamic(name, desc.getDescriptor(), bsm, bsmArgs.toArray());
    }

    public static Handle parseHandle(AnnotationNode handle) {
        Integer tag = null;
        QualifiedMemberNameAndDesc method = null;
        Boolean itf = Boolean.FALSE;
        for (int i = 0; i < handle.values.size(); i += 2) {
            String key = (String) handle.values.get(i);
            Object val = handle.values.get(i + 1);
            switch (key) {
                case "tag":
                    tag = (Integer) val;
                    break;
                case "method":
                    if (method != null) {
                        throw new IllegalArgumentException("Handle annotation cannot have both a method and a field");
                    }
                    method = MethodRefParser.parseQualifiedMethodRef((AnnotationNode) val);
                    break;
                case "field":
                    if (method != null) {
                        throw new IllegalArgumentException("Handle annotation cannot have both a method and a field");
                    }
                    method = FieldRefParser.parseQualifiedFieldRef((AnnotationNode) val);
                case "isInterface":
                    itf = (Boolean) val;
                    break;
            }
        }
        if (tag == null) {
            throw new IllegalArgumentException("Handle annotation must have a tag");
        }
        if (method == null) {
            throw new IllegalArgumentException("Handle annotation must have a method or field");
        }
        return new Handle(tag, method.owner.getInternalName(), method.name, method.desc.getDescriptor(), itf);
    }

    public static JumpInsnNode parseJumpInsn(AnnotationNode insn, Map<String, LabelNode> labelMap) {
        Integer opcode = null;
        String label = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "opcode":
                    opcode = (Integer) val;
                    break;
                case "label":
                    label = (String) val;
                    break;
            }
        }
        if (opcode == null) {
            throw new IllegalArgumentException("JumpInsn annotation must have an opcode");
        }
        if (label == null) {
            throw new IllegalArgumentException("JumpInsn annotation must have a label");
        }
        LabelNode l = labelMap.computeIfAbsent(label, k -> new LabelNode());
        return new JumpInsnNode(opcode, l);
    }

    public static LdcInsnNode parseLdcInsn(AnnotationNode insn) {
        if (insn.values.size() != 2) {
            throw new IllegalArgumentException("Invalid LdcInsn, must only have 1 key and 1 value");
        }
        String key = (String) insn.values.get(0);
        Object val = insn.values.get(1);
        switch (key) {
            case "classValue":
                return new LdcInsnNode(ClassRefParser.parseClassRef((AnnotationNode) val));
            case "handleValue":
                return new LdcInsnNode(parseHandle((AnnotationNode) val));
            case "constantDynamicValue":
                return new LdcInsnNode(parseConstantDynamic((AnnotationNode) val));
        }
        return new LdcInsnNode(val);
    }

    public static IincInsnNode parseIincInsn(AnnotationNode insn) {
        Integer var = null;
        Integer incr = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "var":
                    var = (Integer) val;
                    break;
                case "incr":
                    incr = (Integer) val;
                    break;
            }
        }
        if (var == null) {
            throw new IllegalArgumentException("IincInsn annotation must have a var");
        }
        if (incr == null) {
            throw new IllegalArgumentException("IincInsn annotation must have an incr");
        }
        return new IincInsnNode(var, incr);
    }

    public static TableSwitchInsnNode parseTableSwitchInsn(AnnotationNode insn, Map<String, LabelNode> labelMap) {
        Integer min = null;
        Integer max = null;
        String dflt = null;
        List<String> labels = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "min":
                    min = (Integer) val;
                    break;
                case "max":
                    max = (Integer) val;
                    break;
                case "defaultLabel":
                    dflt = (String) val;
                    break;
                case "labels":
                    labels = (List<String>) val;
                    break;
            }
        }
        if (min == null) {
            throw new IllegalArgumentException("TableSwitchInsn annotation must have a min");
        }
        if (max == null) {
            throw new IllegalArgumentException("TableSwitchInsn annotation must have a max");
        }
        if (dflt == null) {
            throw new IllegalArgumentException("TableSwitchInsn annotation must have a dflt");
        }
        if (labels == null) {
            throw new IllegalArgumentException("TableSwitchInsn annotation must have labels");
        }
        LabelNode d = labelMap.computeIfAbsent(dflt, k -> new LabelNode());
        List<LabelNode> l = new ArrayList<>();
        for (String label : labels) {
            l.add(labelMap.computeIfAbsent(label, k -> new LabelNode()));
        }
        return new TableSwitchInsnNode(min, max, d, l.toArray(new LabelNode[0]));
    }

    public static LookupSwitchInsnNode parseLookupSwitchInsn(AnnotationNode insn, Map<String, LabelNode> labelMap) {
        String dflt = null;
        List<Integer> keys = null;
        List<String> labels = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "defaultLabel":
                    dflt = (String) val;
                    break;
                case "pairs":
                    List<AnnotationNode> pairs = (List<AnnotationNode>) val;
                    keys = new ArrayList<>();
                    labels = new ArrayList<>();
                    for (AnnotationNode pair : pairs) {
                        parsePair(pair, keys, labels);
                    }
                    break;
            }
        }
        if (dflt == null) {
            throw new IllegalArgumentException("LookupSwitchInsn annotation must have a defaultLabel");
        }
        if (keys == null) {
            throw new IllegalArgumentException("LookupSwitchInsn annotation must have pairs");
        }
        LabelNode d = labelMap.computeIfAbsent(dflt, k -> new LabelNode());
        List<LabelNode> l = new ArrayList<>();
        for (String label : labels) {
            l.add(labelMap.computeIfAbsent(label, k -> new LabelNode()));
        }
        return new LookupSwitchInsnNode(d, keys.stream().mapToInt(i -> i).toArray(), l.toArray(new LabelNode[0]));
    }

    private static void parsePair(AnnotationNode pair, List<Integer> keys, List<String> values) {
        Integer k = null;
        String l = null;
        for (int i = 0; i < pair.values.size(); i += 2) {
            String key = (String) pair.values.get(i);
            Object val = pair.values.get(i + 1);
            switch (key) {
                case "key":
                    k = (Integer) val;
                    break;
                case "label":
                    l = (String) val;
                    break;
            }
        }
        if (k == null) {
            throw new IllegalArgumentException("LookupSwitchInsn Pair annotation must have a key");
        }
        if (l == null) {
            throw new IllegalArgumentException("LookupSwitchInsn Pair annotation must have a label");
        }
        keys.add(k);
        values.add(l);
    }

    public static MultiANewArrayInsnNode parseMultiANewArrayInsn(AnnotationNode insn) {
        Type type = null;
        Integer dims = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "type":
                    type = ClassRefParser.parseClassRef((AnnotationNode) val);
                    break;
                case "dims":
                    dims = (Integer) val;
                    break;
            }
        }
        if (type == null) {
            throw new IllegalArgumentException("MultiANewArrayInsn annotation must have a type");
        }
        if (dims == null) {
            throw new IllegalArgumentException("MultiANewArrayInsn annotation must have dims");
        }
        return new MultiANewArrayInsnNode(type.getDescriptor(), dims);
    }

    public static AnnotationNode parseAnnotationInsn(AnnotationNode insn) {
        throw new UnsupportedOperationException("AnnotationInsn not supported yet");
    }

    public static TryCatchBlockNode parseTryCatchBlock(AnnotationNode insn, Map<String, LabelNode> labelMap) {
        String start = null;
        String end = null;
        String handler = null;
        Type type = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "start":
                    start = (String) val;
                    break;
                case "end":
                    end = (String) val;
                    break;
                case "handler":
                    handler = (String) val;
                    break;
                case "type":
                    type = ClassRefParser.parseClassRef((AnnotationNode) val);
                    break;
            }
        }
        if (start == null) {
            throw new IllegalArgumentException("TryCatchBlock annotation must have a start");
        }
        if (end == null) {
            throw new IllegalArgumentException("TryCatchBlock annotation must have an end");
        }
        if (handler == null) {
            throw new IllegalArgumentException("TryCatchBlock annotation must have a handler");
        }
        if (type == null) {
            throw new IllegalArgumentException("TryCatchBlock annotation must have a type");
        }
        return new TryCatchBlockNode(labelMap.computeIfAbsent(start, k -> new LabelNode()), labelMap.computeIfAbsent(end, k -> new LabelNode()), labelMap.computeIfAbsent(handler, k -> new LabelNode()), type.getInternalName());
    }

    public static LocalVariableNode parseLocalVariable(AnnotationNode insn, Map<String, LabelNode> labelMap) {
        String name = null;
        Type desc = null;
        String signature = null;
        String start = null;
        String end = null;
        Integer index = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "name":
                    name = (String) val;
                    break;
                case "desc":
                    desc = ClassRefParser.parseClassRef((AnnotationNode) val);
                    break;
                case "signature":
                    signature = (String) val;
                    break;
                case "start":
                    start = (String) val;
                    break;
                case "end":
                    end = (String) val;
                    break;
                case "index":
                    index = (Integer) val;
                    break;
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("LocalVariable annotation must have a name");
        }
        if (desc == null) {
            throw new IllegalArgumentException("LocalVariable annotation must have a desc");
        }
        if (start == null) {
            throw new IllegalArgumentException("LocalVariable annotation must have a start");
        }
        if (end == null) {
            throw new IllegalArgumentException("LocalVariable annotation must have an end");
        }
        if (index == null) {
            throw new IllegalArgumentException("LocalVariable annotation must have an index");
        }
        return new LocalVariableNode(name, desc.getDescriptor(), signature, labelMap.computeIfAbsent(start, k -> new LabelNode()), labelMap.computeIfAbsent(end, k -> new LabelNode()), index);
    }

    public static LocalVariableAnnotationNode parseLocalVariableAnnotation(AnnotationNode insn) {
        throw new UnsupportedOperationException("LocalVariableAnnotation not supported yet");
    }

    public static LineNumberNode parseLineNumber(AnnotationNode insn) {
        Integer line = null;
        String label = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "line":
                    line = (Integer) val;
                    break;
                case "label":
                    label = (String) val;
                    break;
            }
        }
        if (line == null) {
            throw new IllegalArgumentException("LineNumber annotation must have a line");
        }
        if (label == null) {
            throw new IllegalArgumentException("LineNumber annotation must have a label");
        }
        return new LineNumberNode(line, new LabelNode());
    }

    public static void parseMaxs(AnnotationNode insn, MethodNode methodNode) {
        Integer maxStack = null;
        Integer maxLocals = null;
        for (int i = 0; i < insn.values.size(); i += 2) {
            String key = (String) insn.values.get(i);
            Object val = insn.values.get(i + 1);
            switch (key) {
                case "maxStack":
                    maxStack = (Integer) val;
                    break;
                case "maxLocals":
                    maxLocals = (Integer) val;
                    break;
            }
        }
        if (maxStack == null) {
            throw new IllegalArgumentException("Maxs annotation must have a maxStack");
        }
        if (maxLocals == null) {
            throw new IllegalArgumentException("Maxs annotation must have a maxLocals");
        }
        methodNode.maxStack = maxStack;
        methodNode.maxLocals = maxLocals;
    }

}
