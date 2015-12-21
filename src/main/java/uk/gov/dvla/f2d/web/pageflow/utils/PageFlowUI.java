package uk.gov.dvla.f2d.web.pageflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;

public final class PageFlowUI
{
    /**
     * Get the full structure of the pre-populated list of conditions and questions.
     */
    public static String getMedicalForm() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(PageFlowCacheManager.getMedicalForm());
    }
}
