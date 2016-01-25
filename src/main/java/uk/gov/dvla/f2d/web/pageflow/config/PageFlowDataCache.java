package uk.gov.dvla.f2d.web.pageflow.config;

import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.utils.MapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.SUPPORTED_SERVICES;

final class PageFlowDataCache
{
    private static final String PAGEFLOW_PREFIX     = "pageflow-";
    private static final String JSON_SUFFIX         = ".json";

    static MedicalForm getMedicalForm(final String service) {
        try {
            if(!(Arrays.asList(SUPPORTED_SERVICES).contains(service))) {
                throw new IllegalArgumentException("Service is not currently supported: ["+service+"].");
            }

            final String resourceToLoad = (PAGEFLOW_PREFIX + service + JSON_SUFFIX);

            ClassLoader classLoader = PageFlowDataCache.class.getClassLoader();
            InputStream resourceStream = classLoader.getResource(resourceToLoad).openStream();

            return MapUtils.mapStreamToModel(resourceStream);

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
