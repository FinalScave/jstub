package com.tiecode.stub;

import com.tiecode.stub.util.BundleKey;
import com.tiecode.stub.util.BundleUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Stub生成命令实体
 *
 * @author Scave
 */
public class StubCommand {

    private static final String COMMAND_OUTPUT = "-out";
    private static final String COMMAND_LEVEL = "-level";
    private static final String COMMAND_HELP = "--help";

    private boolean printHelp;
    private String outPath;
    private Level level = Level.PROTECTED;
    private Set<File> files = new HashSet<>();

    public StubCommand() {
    }

    public StubCommand(String[] args) {
        processCommand(args);
    }

    private void processCommand(String[] args) {
        if (args.length == 0) {
            this.printHelp = true;
            throw new RuntimeException(BundleUtil.getCommandText(BundleKey.no_error));
        }
        for (int i = 0, length = args.length; i < length; i++) {
            switch (args[i]) {
                case COMMAND_OUTPUT:
                    if (i + 1 < length) {
                        this.outPath = args[++i];
                    } else {
                        throw new RuntimeException(BundleUtil.getCommandText(BundleKey.out_error));
                    }
                    break;
                case COMMAND_LEVEL:
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
                case COMMAND_HELP:
                    this.printHelp = true;
                    return;
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
        if (!printHelp && this.outPath == null) {
            throw new RuntimeException(BundleUtil.getCommandText(BundleKey.out_must_error));
        }
    }

    public void printHelp() {
        System.out.println(BundleUtil.getCommandText(BundleKey.help_head));
        System.out.println(BundleUtil.getCommandText(BundleKey.usage));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_options_head));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_out));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_level));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_level0));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_level1));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_level2));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_command));
    }

    public boolean isPrintHelp() {
        return printHelp;
    }

    public void setPrintHelp(boolean printHelp) {
        this.printHelp = printHelp;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
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
