package uk.gov.dvla.f2d.web.pageflow.utils;

import static uk.gov.dvla.f2d.model.constants.Constants.isLanguageSupported;
import static uk.gov.dvla.f2d.model.constants.Constants.isServiceSupported;

public class ServiceUtils
{
    private ServiceUtils() {
        super();
    }

    public static void checkServiceSupported(final String service) {
        if(!(isServiceSupported(service))) {
            throw new IllegalArgumentException("Service is not currently supported: " + service);
        }
    }

    public static void checkLanguageSupported(final String language) {
        if(!(isLanguageSupported(language))) {
            throw new IllegalArgumentException("Language is not currently supported: " + language);
        }
    }
}
