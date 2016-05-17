package uk.gov.dvla.f2d.web.pageflow.helpers;

import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;

import static uk.gov.dvla.f2d.model.constants.StringConstants.*;

public class FormHelper
{
    private FormHelper() {
        super();
    }

    public static String capitalise(MedicalQuestion question) {
        String[] parts = question.getID().split(HYPHEN);
        String result = EMPTY;
        for(String part : parts) {
            result += part.substring(0, 1).toUpperCase() + part.substring(1);
        }
        return result;
    }
}
