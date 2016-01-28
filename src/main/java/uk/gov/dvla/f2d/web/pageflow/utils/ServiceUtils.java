package uk.gov.dvla.f2d.web.pageflow.utils;

import java.util.Arrays;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.SUPPORTED_SERVICES;

public class ServiceUtils
{
    private ServiceUtils() {
        super();
    }

    public static void checkServiceSupported(final String service) {
        if(!(Arrays.asList(SUPPORTED_SERVICES).contains(service))) {
            throw new IllegalArgumentException("Service is not currently supported: " + service);
        }
    }
}
