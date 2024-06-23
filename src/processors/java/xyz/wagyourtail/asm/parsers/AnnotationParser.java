package xyz.wagyourtail.asm.parsers;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import xyz.wagyourtail.asm.parsers.ref.ClassRefParser;
import xyz.wagyourtail.asm.parsers.ref.EnumRefParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationParser {

    public static AnnotationNode parseAnnotationASM(AnnotationNode annotation) {
        Type owner = null;
        Map<String, List<Object>> arrayValues = null;
        Map<String, Object> values = null;

        for (int i = 0; i < annotation.values.size(); i += 2) {
            String key = (String) annotation.values.get(i);
            Object value = annotation.values.get(i + 1);
            switch (key) {
                case "owner":
                    owner = ClassRefParser.parseClassRef((AnnotationNode) value);
                    break;
                case "arrayValues":
                    arrayValues = parseArrayValues((List<AnnotationNode>) value);
                    break;
                case "values":
                    values = parseValues((List<AnnotationNode>) value);
                    break;
            }
        }
        if (owner == null) {
            throw new IllegalArgumentException("AnnotationASM annotation must have an owner");
        }
        AnnotationNode node = new AnnotationNode(owner.getDescriptor());
        node.values = new ArrayList<>();
        if (values != null) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                node.values.add(entry.getKey());
                node.values.add(entry.getValue());
            }
        }
        if (arrayValues != null) {
            for (Map.Entry<String, List<Object>> entry : arrayValues.entrySet()) {
                node.values.add(entry.getKey());
                node.values.add(entry.getValue());
            }
        }
        return node;
    }

    public static Map<String, List<Object>> parseArrayValues(List<AnnotationNode> arrayValues) {
        Map<String, List<Object>> map = new HashMap<>();
        for (AnnotationNode value : arrayValues) {
            String key = null;
            List<Object> parsed = null;
            for (int i = 0; i < value.values.size(); i += 2) {
                String k = (String) value.values.get(i);
                Object v = value.values.get(i + 1);
                switch (k) {
                    case "key":
                        key = (String) v;
                        break;
                    case "value":
                        parsed = new ArrayList<>();
                        for (AnnotationNode annotationNode : (List<AnnotationNode>) v) {
                            parsed.add(parseValue(annotationNode));
                        }
                        break;
                }
            }
            if (key == null) {
                throw new IllegalArgumentException("AnnotationASM.ArrayValue must have a key");
            }
            if (parsed == null) {
                throw new IllegalArgumentException("AnnotationASM.ArrayValue must have a value");
            }
            map.put(key, parsed);
        }
        return map;
    }

    public static Map<String, Object> parseValues(List<AnnotationNode> values) {
        Map<String, Object> map = new HashMap<>();
        for (AnnotationNode value : values) {
            String key = null;
            Object parsed = null;
            for (int i = 0; i < value.values.size(); i += 2) {
                String k = (String) value.values.get(i);
                Object v = value.values.get(i + 1);
                switch (k) {
                    case "key":
                        key = (String) v;
                        break;
                    case "value":
                        parsed = parseValue((AnnotationNode) v);
                        break;
                }
            }
            if (key == null) {
                throw new IllegalArgumentException("AnnotationASM.KeyValue must have a key");
            }
            if (parsed == null) {
                throw new IllegalArgumentException("AnnotationASM.KeyValue must have a value");
            }
            map.put(key, parsed);
        }
        return map;
    }

    public static Object parseValue(AnnotationNode value) {
        if (value.values.size() != 2) {
            throw new IllegalArgumentException("AnnotationASM.KeyValue must have a single key and value");
        }

        String key = (String) value.values.get(0);
        Object val = value.values.get(1);

        switch (key) {
            case "annotationValue":
                return parseAnnotationASM((AnnotationNode) val);
            case "classRef":
                return ClassRefParser.parseClassRef((AnnotationNode) val);
            case "enumRef":
                 return EnumRefParser.parseEnumRef((AnnotationNode) val);
            case "stringValue":
            case "doubleValue":
            case "longValue":
            case "intValue":
            case "floatValue":
            case "booleanValue":
                return val;
            default:
                throw new IllegalArgumentException("Unknown key: " + key);
        }
    }

}
