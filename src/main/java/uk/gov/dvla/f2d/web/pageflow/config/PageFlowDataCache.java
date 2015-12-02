package uk.gov.dvla.f2d.web.pageflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestionnaire;

import java.io.IOException;
import java.io.InputStream;

final class PageFlowDataCache
{
    private static final String PAGEFLOW_MODEL_FILE     = "pageflow.json";

    private static PageFlowDataCache instance;

    private MedicalQuestionnaire medical;

    private PageFlowDataCache() {
        initialise();
    }

    static synchronized PageFlowDataCache getInstance() {
        if(instance == null) {
            instance = new PageFlowDataCache();
        }
        return instance;
    }

    private void initialise() {
        try {
            // We need to import all the supported medical conditions.
            medical = loadDataIntoInternalCache();

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private MedicalQuestionnaire loadDataIntoInternalCache() throws IOException {
        return loadMedicalQuestionnaireFromLocalJsonResource();
    }

    private MedicalQuestionnaire loadMedicalQuestionnaireFromLocalJsonResource() throws IOException {
        InputStream resourceStream = getClass().getClassLoader().getResource(PAGEFLOW_MODEL_FILE).openStream();
        return new ObjectMapper().readValue(resourceStream, MedicalQuestionnaire.class);
    }

    MedicalQuestionnaire getMedicalQuestionnaire() {
        return medical;
    }
}
