package com.qiplat.toolchain.jstub;

import com.qiplat.toolchain.jstub.util.BundleKey;
import com.qiplat.toolchain.jstub.util.BundleUtil;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            throw new RuntimeException(BundleUtil.getCommandText(BundleKey.no_error));
        }
        try {
            StubCommand command = new StubCommand(args);
            StubGenerator generator = new StubGenerator(command);
            generator.runStubGen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println(BundleUtil.getCommandText(BundleKey.help_head));
        System.out.println(BundleUtil.getCommandText(BundleKey.usage));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_options_head));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_out));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_level));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_level0));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_level1));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_level2));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_verbose));
        System.out.println(BundleUtil.getCommandText(BundleKey.help_command));
    }
}