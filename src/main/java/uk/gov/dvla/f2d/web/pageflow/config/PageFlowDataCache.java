package uk.gov.dvla.f2d.web.pageflow.config;

import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.utils.MapUtils;
import uk.gov.dvla.f2d.web.pageflow.utils.ServiceUtils;

import java.io.IOException;
import java.io.InputStream;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

final class PageFlowDataCache
{
    static MedicalForm getMedicalForm(final String service) {
        try {
            ServiceUtils.checkServiceSupported(service);

            final String resourceToLoad = (PAGEFLOW_PREFIX + HYPHEN_SYMBOL + service + JSON_SUFFIX);

            ClassLoader classLoader = PageFlowDataCache.class.getClassLoader();
            InputStream resourceStream = classLoader.getResource(resourceToLoad).openStream();

            return MapUtils.mapStreamToModel(resourceStream);

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
