package com.tiecode.stub;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            StubCommand command = new StubCommand(args);
            StubGenerator generator = new StubGenerator(command);
            generator.runStubGen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}