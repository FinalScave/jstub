package com.qiplat.toolchain.jstub;

import com.qiplat.toolchain.jstub.util.BundleKey;
import com.qiplat.toolchain.jstub.util.BundleUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Stub生成命令实体
 *
 * @author Scave
 */
public class StubCommand {

    private static final String OUT_PATH = "-out";
    private static final String MODIFIER_LEVEL = "-level";
    private static final String VERBOSE = "-v";
    private static final String HELP = "--help";

    private boolean printHelp;
    private String outputPath;
    private Level level = Level.PROTECTED;
    private boolean verbose;
    private Set<File> files = new HashSet<>();

    public StubCommand() {
    }

    public StubCommand(String[] args) {
        processCommand(args);
    }

    private void processCommand(String[] args) {
        for (int i = 0, length = args.length; i < length; i++) {
            switch (args[i]) {
                case OUT_PATH:
                    if (i + 1 < length) {
                        this.outputPath = args[++i];
                    } else {
                        throw new RuntimeException(BundleUtil.getCommandText(BundleKey.out_error));
                    }
                    break;
                case MODIFIER_LEVEL:
                    if (i + 1 < length) {
                        try {
                            int num = Integer.parseInt(args[++i]);
                            if (num == 0) {
                                this.level = Level.PRIVATE;
                            } else if (num == 1) {
                                this.level = Level.PROTECTED;
                            } else if (num == 2) {
                                this.level = Level.PUBLIC;
                            } else {
                                throw new RuntimeException(BundleUtil.getCommandText(BundleKey.level_param_error));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(BundleUtil.getCommandText(BundleKey.level_param_error));
                        }
                    } else {
                        throw new RuntimeException(BundleUtil.getCommandText(BundleKey.level_error));
                    }
                    break;
                case HELP:
                    this.printHelp = true;
                    return;
                case VERBOSE:
                    this.verbose = true;
                    break;
                default:
                    String path = args[i];
                    File file = new File(path);
                    if (file.exists()) {
                        files.add(file);
                    } else {
                        throw new RuntimeException(BundleUtil.getFormatCommandText(BundleKey.file_error, file.getAbsolutePath()));
                    }
                    break;
            }
        }
        if (!printHelp && this.outputPath == null) {
            throw new RuntimeException(BundleUtil.getCommandText(BundleKey.out_must_error));
        }
    }

    public boolean isPrintHelp() {
        return printHelp;
    }

    public void setPrintHelp(boolean printHelp) {
        this.printHelp = printHelp;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public enum Level {
        PRIVATE,
        PROTECTED,
        PUBLIC
    }
}
