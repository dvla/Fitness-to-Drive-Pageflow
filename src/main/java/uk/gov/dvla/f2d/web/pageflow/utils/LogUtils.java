package uk.gov.dvla.f2d.web.pageflow.utils;

import java.io.OutputStream;
import java.io.PrintStream;

public class LogUtils
{
    private static final LogUtils INSTANCE = new LogUtils();

    private static OutputStream printer;

    private LogUtils() {
        printer = System.out;
    }

    public static void debug(Class myClass, String message) {
        final String output = "DEBUG::" + myClass.getSimpleName() + " -> " + message;
        INSTANCE.debugToConsole(output);
    }

    private void debugToConsole(final String message) {
        ((PrintStream)printer).println(message);
    }

    private void debugToFile(final String message) {
        // Not yet implemented.
    }
}
