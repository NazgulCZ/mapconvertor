package org.nazgul.mapconvertor;

public class Main {

    public static final String version = "0.2";

    public static void main(String[] args) throws Exception {
        MapConvertorConsole mapConvertorConsole = new MapConvertorConsole(args);
        mapConvertorConsole.run();
    }

}
