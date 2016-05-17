package uk.gov.dvla.f2d.web.pageflow.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.loaders.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.JSON_SUFFIX;

final class PageFlowDataCache
{
    private static final String SUPPORTED_PREFIX        = "supported";

    PageFlowDataCache() {
        super();
    }

    Map<String, MedicalCondition> getSupportedConditions(final String service) throws IOException {
        final String resourceToLoad = (SUPPORTED_PREFIX + HYPHEN + service + JSON_SUFFIX);

        InputStream stream = ResourceLoader.load(resourceToLoad);

        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        TypeReference<Map<String, MedicalCondition>> reference =
            new TypeReference<Map<String, MedicalCondition>>() {};

        return mapper.readValue(stream, reference);
    }
}
