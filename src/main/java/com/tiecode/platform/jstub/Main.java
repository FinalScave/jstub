package com.tiecode.platform.jstub;

import javax.xml.ws.Action;
import java.io.IOException;

public class Main {

    @Action
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