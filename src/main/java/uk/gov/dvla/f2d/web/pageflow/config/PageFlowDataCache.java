package uk.gov.dvla.f2d.web.pageflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;

import java.io.IOException;
import java.io.InputStream;

final class PageFlowDataCache
{
    private static final String PAGEFLOW_MODEL_FILE     = "pageflow.json";

    static MedicalForm getMedicalForm() {
        try {
            ClassLoader classLoader = PageFlowDataCache.class.getClassLoader();
            InputStream resourceStream = classLoader.getResource(PAGEFLOW_MODEL_FILE).openStream();
            return new ObjectMapper().readValue(resourceStream, MedicalForm.class);

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
