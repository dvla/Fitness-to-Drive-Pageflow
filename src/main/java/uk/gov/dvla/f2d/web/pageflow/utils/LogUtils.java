package uk.gov.dvla.f2d.web.pageflow.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.PrintStream;

public class LogUtils
{
    private static final LogUtils INSTANCE = new LogUtils();

    private static OutputStream printer;

    private static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    private LogUtils() {
        printer = System.out;
    }


    public static void debug(Class myClass, String message) {
        final String output = "DEBUG::" + myClass.getSimpleName() + " -> " + message;
        logger.debug(output);
    }

    private void debugToConsole(final String message) {
        ((PrintStream)printer).println(message);
    }

    private void debugToFile(final String message) {
        // Not yet implemented.
    }
}
