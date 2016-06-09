package uk.gov.dvla.f2d.web.pageflow.helpers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;

import java.util.Map;

public class FormHelperTest extends TestCase
{
    private static final String DIABETES_CONDITION      = "diabetes";
    private static final String DIABETES_QUESTION       = "diabetes-with-insulin";

    /**
     * Create the test case
     * @param testName name of the test case
     */
    public FormHelperTest(String testName ) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(FormHelperTest.class);
    }

    /**
     * This test check the "capitalise" method of the FormHelper class.
     */
    public void testCapitalise() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(Service.NOTIFY);
        assertNotNull(form);

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.getName());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        MedicalQuestion question = form.getMedicalCondition().getQuestions().get(DIABETES_QUESTION);

        String result = FormHelper.capitalise(question);

        assertEquals(result, "DiabetesWithInsulin");
    }
}
