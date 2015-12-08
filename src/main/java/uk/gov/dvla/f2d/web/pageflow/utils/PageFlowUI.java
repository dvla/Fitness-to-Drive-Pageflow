package uk.gov.dvla.f2d.web.pageflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;

public final class PageFlowUI
{
    /**
     * Get the full structure of the pre-populated list of conditions and questions.
     * @return
     * @throws JsonProcessingException
     */
    public static String getFullQuestionnaire() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(PageFlowCacheManager.getQuestionnaire());
    }

    /**
     * Get an iterative, displayable list of all the currently supported medical conditions.
     * @return List<MedicalCondition> of medical conditions.
     */
    public static String getSupportedConditions() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(PageFlowCacheManager.getConditions().values());
    }

    /**
     * Once a user has selected their medical condition, we must retrieve the configuration
     * from the cache and then we can start to build a pathway through the application.
     * @param ID
     * @return
     * @throws JsonProcessingException
     */
    public static String getConditionByID(final String ID) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(PageFlowCacheManager.getConditionByID(ID));
    }
}
