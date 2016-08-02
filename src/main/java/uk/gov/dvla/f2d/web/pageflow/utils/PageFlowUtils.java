package uk.gov.dvla.f2d.web.pageflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.utils.StringUtils;

import java.io.IOException;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;

public final class PageFlowUtils
{
    public static MedicalForm mapStringToModel(final String jsonModel) throws IOException {
        return new ObjectMapper().readValue(jsonModel, MedicalForm.class);
    }

    public static String mapModelToString(MedicalForm form) throws JsonProcessingException{
        return new ObjectMapper().writeValueAsString(form);
    }

    public static String capitalise(final String page) {
        return StringUtils.splitAndCapitalise(page, HYPHEN);
    }

    public static String capitalise(MedicalQuestion question) {
        return capitalise(question.getID());
    }

}
