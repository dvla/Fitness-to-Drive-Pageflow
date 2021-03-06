package uk.gov.dvla.f2d.web.pageflow.cache;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.loaders.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import static uk.gov.dvla.f2d.model.constants.StringConstants.*;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

final class PageFlowDataCache
{
    private Map<Service, String> services;

    PageFlowDataCache() {
        super();
    }

    void initialise() throws IOException {
        services = new TreeMap<>();
        for(Service service : Service.values()) {
            Map<String, MedicalCondition> conditions = loadMedicalConditions(service);
            services.put(service, new ObjectMapper().writeValueAsString(conditions));
        }
    }

    private Map<String, MedicalCondition> loadMedicalConditions(Service service) throws IOException {
        String resourceToLoad = (service.getName() + FORWARD_SLASH + SUPPORTED_PREFIX + JSON_SUFFIX);
        InputStream stream = ResourceLoader.load(resourceToLoad);

        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        TypeReference<Map<String, MedicalCondition>> reference =
                new TypeReference<Map<String, MedicalCondition>>() {};

        return mapper.readValue(stream, reference);
    }


    Map<String, MedicalCondition> getSupportedConditions(Service service) {
        try {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            TypeReference<Map<String, MedicalCondition>> reference =
                new TypeReference<Map<String, MedicalCondition>>() {};

            return mapper.readValue(services.get(service), reference);

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
