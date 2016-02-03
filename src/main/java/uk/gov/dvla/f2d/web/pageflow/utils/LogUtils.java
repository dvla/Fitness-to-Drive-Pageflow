package uk.gov.dvla.f2d.web.pageflow.utils;

public class LogUtils
{
    public static void debug(Class myClass, String message) {
        System.out.println(myClass.getSimpleName()+ " -> " + message);
    }
}
