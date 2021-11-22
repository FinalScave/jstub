package com.tiecode.stub;

import org.objectweb.asm.*;
import java.io.*;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * Stub包生成器
 *
 * @author Scave
 */
public class StubGenerator {

    private final StubCommand command;

    public StubGenerator(StubCommand command) {
        this.command = command;
    }

    /**
     * 生成stub
     */
    public void runStubGen() throws IOException {
        if (command.isPrintHelp()) {
            command.printHelp();
            return;
        }
        String outPath = command.getOutPath();
        JarOutputStream outStream = new JarOutputStream(new FileOutputStream(outPath));
        processFiles(command.getFiles(), outStream);
        outStream.close();
    }

    private void processFiles(Set<File> files, JarOutputStream outStream) throws IOException {
        for (File file : files) {
            processFile(file, outStream);
        }
    }

    private void processFile(File file, JarOutputStream outStream) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    processFile(child, outStream);
                }
            }
        } else if (file.getName().endsWith(".jar")) {
            JarInputStream inStream = new JarInputStream(new FileInputStream(file));
            JarEntry entry = inStream.getNextJarEntry();
            while (entry != null) {
                if (!entry.isDirectory()) {
                    processStream(inStream, outStream);
                }
                inStream.closeEntry();
                entry = inStream.getNextJarEntry();
            }
            inStream.close();
        } else if (file.getName().endsWith(".class")) {
            InputStream inStream = new FileInputStream(file);
            processStream(inStream, outStream);
            inStream.close();
        }
    }

    private void processStream(InputStream inStream, JarOutputStream outStream) throws IOException {
        StubClassReader reader = new StubClassReader(inStream);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        reader.accept(writer, ClassReader.SKIP_DEBUG);
        String className = reader.getClassName();
        String entryName = className.replace('.', '/') + ".class";
        JarEntry entry = new JarEntry(entryName);
        outStream.putNextEntry(entry);
        byte[] bytes = writer.toByteArray();
        outStream.write(bytes);
        outStream.closeEntry();
    }

    class StubClassReader extends ClassReader {

        StubClassVisitor visitor;

        public StubClassReader(InputStream inputStream) throws IOException {
            super(inputStream);
        }

        @Override
        public void accept(ClassVisitor classVisitor, int flags) {
            super.accept(visitor = new StubClassVisitor(Opcodes.ASM8, classVisitor), flags);
        }

        public String getClassName() {
            return visitor.getClassName();
        }
    }

    class StubClassVisitor extends ClassVisitor {

        private String className;

        public StubClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            this.className = name;
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            FieldVisitor fieldVisitor = super.visitField(access, name, descriptor, signature, value);
            switch (command.getLevel()) {
                case PUBLIC:
                    if (Modifier.isProtected(access) || Modifier.isPrivate(access)) {
                        return null;
                    }
                    return fieldVisitor;
                case PROTECTED:
                    if (Modifier.isPrivate(access)) {
                        return null;
                    }
                    return fieldVisitor;
            }
            return fieldVisitor;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (Modifier.isAbstract(access)) {
                return methodVisitor;
            }
            if (!"<init>".equals(name)) {
                switch (command.getLevel()) {
                    case PUBLIC:
                        if (Modifier.isProtected(access) || Modifier.isPrivate(access)) {
                            return null;
                        }
                        return methodVisitor;
                    case PROTECTED:
                        if (Modifier.isPrivate(access)) {
                            return null;
                        }
                        return methodVisitor;
                }
            }
            if (methodVisitor != null) {
                return new MethodVisitor(Opcodes.ASM8) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        return methodVisitor.visitAnnotation(desc, visible);
                    }

                    @Override
                    public void visitCode() {
                        String internalName = Type.getInternalName(UnsupportedOperationException.class);
                        methodVisitor.visitTypeInsn(Opcodes.NEW, internalName);
                        methodVisitor.visitInsn(Opcodes.DUP);
                        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, internalName, "<init>", "()V", false);
                        methodVisitor.visitInsn(Opcodes.ATHROW);
                    }
                };
            }
            return null;
        }

        public String getClassName() {
            return className;
        }
    }

}
