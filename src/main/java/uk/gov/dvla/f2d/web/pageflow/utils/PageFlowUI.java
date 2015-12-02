package uk.gov.dvla.f2d.web.pageflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;

public final class PageFlowUI
{
    private PageFlowUI() {
       // Nothing to do here.
    }

    /**
     * Get an iterative, displayable list of all the currently supported medical conditions.
     * @return List<MedicalCondition> of medical conditions.
     */
    public static String getSupportedConditions() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(PageFlowCacheManager.getConditions().values());
    }

    public static String getConditionByID(final String ID) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(PageFlowCacheManager.getConditionByID(ID));
    }
}
