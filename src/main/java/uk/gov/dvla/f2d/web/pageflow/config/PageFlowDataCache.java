package uk.gov.dvla.f2d.web.pageflow.config;

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

import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.JSON_SUFFIX;

final class PageFlowDataCache
{
    private static final String SUPPORTED_PREFIX        = "supported";

    /*
    private Map<Service, Map<String, MedicalCondition>> services;

    PageFlowDataCache() {
        super();
    }

    void initialise() throws IOException {
        services = new TreeMap<>();

        for(Service service : Service.values()) {
            Map<String, MedicalCondition> conditions = loadMedicalConditions(service);
            services.put(service, conditions);
        }
    }

    private Map<String, MedicalCondition> loadMedicalConditions(Service service) throws IOException {
        String resourceToLoad = (SUPPORTED_PREFIX + HYPHEN + service.getName() + JSON_SUFFIX);

        InputStream stream = ResourceLoader.load(resourceToLoad);

        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        TypeReference<Map<String, MedicalCondition>> reference =
                new TypeReference<Map<String, MedicalCondition>>() {};

        return mapper.readValue(stream, reference);
    }

    Map<String, MedicalCondition> getSupportedConditions(Service service) {
        Map<Service, Map<String, MedicalCondition>> local = new TreeMap();
        local.putAll(services);

        return local.get(service);
    }
    */
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

    private InputStream loadConditionsIntoStream(Service service) throws IOException {
        return ResourceLoader.load(SUPPORTED_PREFIX + HYPHEN + service.getName() + JSON_SUFFIX);
    }

    private Map<String, MedicalCondition> loadMedicalConditions(Service service) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        TypeReference<Map<String, MedicalCondition>> reference =
                new TypeReference<Map<String, MedicalCondition>>() {};

        return mapper.readValue(loadConditionsIntoStream(service), reference);
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
