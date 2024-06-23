package xyz.wagyourtail.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class PathProcessor {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: PathProcessor <input(s)> <output>");
            System.exit(1);
        }

        String[] inputs = args[0].split(File.pathSeparator);
        for (String input : inputs) {
            Path inputPath = Paths.get(input);
            if (!Files.exists(inputPath)) continue;
            process(inputPath, Paths.get(args[1]));
        }
    }

    public static void process(Path inputRoot, Path outputRoot) throws IOException {
        try (Stream<Path> files = Files.walk(inputRoot)) {
            files.parallel().forEach(path -> {
                try {
                    if (Files.isDirectory(path)) return;
                    Path parent = path.getParent();
                    if (parent != null) {
                        Files.createDirectories(outputRoot.resolve(inputRoot.relativize(parent).toString()));
                    }

                    if (path.getFileName().toString().endsWith(".class")) {
                        ClassReader cr = new ClassReader(Files.newInputStream(path));
                        ClassNode classNode = new ClassNode();
                        cr.accept(classNode, 0);

                        ClassProcessor.process(classNode);

                        ClassWriter cw = new ClassWriter(cr, 0);
                        classNode.accept(cw);

                        Path output = outputRoot.resolve(inputRoot.relativize(path).toString());
                        Files.write(output, cw.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    } else {
                        Path output = outputRoot.resolve(inputRoot.relativize(path).toString());
                        Files.copy(path, output, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException exception) {
                    throw new UncheckedIOException(exception);
                }
            });
        }
    }

}
