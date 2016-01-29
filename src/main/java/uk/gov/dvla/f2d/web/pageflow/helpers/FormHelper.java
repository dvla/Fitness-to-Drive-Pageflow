package uk.gov.dvla.f2d.web.pageflow.helpers;

import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public class FormHelper
{
    private FormHelper() {
        super();
    }

    public static String capitalise(MedicalQuestion question) {
        String[] parts = question.getID().split(HYPHEN_SYMBOL);
        String result = EMPTY_STRING;
        for(String part : parts) {
            result += part.substring(0, 1).toUpperCase() + part.substring(1);
        }
        return result;
    }
}
