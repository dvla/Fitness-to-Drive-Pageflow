package uk.gov.dvla.f2d.web.pageflow.summary;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public class SummaryAggregatorTest extends TestCase
{
    private static final String ENGLISH_LANGUAGE    = "en";
    private static final String WELSH_LANGUAGE      = "cy";

    private static final String YES_ANSWER          = "Y";
    private static final String NO_ANSWER           = "N";

    private static final String DIABETES_CONDITION  = "diabetes";
    private static final String DIABETES_QUESTION   = "diabetes-with-insulin";

    /**
     * Create the test case
     * @param testName name of the test case
     */
    public SummaryAggregatorTest(String testName ) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(SummaryAggregatorTest.class);
    }

    /**
     * Test the summary for (Notify -> Diabetes With Insulin -> Yes -> English).
     */
    public void testNotifyServiceForDiabetesWithInsulinInEnglish() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        assertNotNull(form);

        assertEquals(NOTIFY_SERVICE, form.getMessageHeader().getService());
        assertEquals(ENGLISH_LANGUAGE, form.getMessageHeader().getLanguage());

        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        SummaryAggregator aggregator = SummaryAggregator.getInstance();
        assertNotNull(aggregator);

        Summary summary = aggregator.aggregate(form);
        assertNotNull(summary);

        Option option = summary.getQuestions().get(DIABETES_QUESTION);
        assertNotNull(option);
        assertTrue(option.getOptions().size() == 2);

        Answer answer = option.getOptions().get(YES_ANSWER);
        assertNotNull(answer);

        String text = answer.getAnswers().get(ENGLISH_LANGUAGE);
        assertEquals(text, "YES, this is the ENGLISH answer to your diabetes with insulin");
    }

    /**
     * Test the summary for (Notify -> Diabetes With Insulin -> No -> English).
     */
    public void testNotifyServiceForDiabetesWithInsulinInWelsh() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        assertNotNull(form);

        form.getMessageHeader().setLanguage(WELSH_LANGUAGE);

        assertEquals(NOTIFY_SERVICE, form.getMessageHeader().getService());
        assertEquals(WELSH_LANGUAGE, form.getMessageHeader().getLanguage());

        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        SummaryAggregator aggregator = SummaryAggregator.getInstance();
        assertNotNull(aggregator);

        Summary summary = aggregator.aggregate(form);
        assertNotNull(summary);

        Option option = summary.getQuestions().get(DIABETES_QUESTION);
        assertNotNull(option);
        assertTrue(option.getOptions().size() == 2);

        Answer answer = option.getOptions().get(NO_ANSWER);
        assertNotNull(answer);

        String text = answer.getAnswers().get(WELSH_LANGUAGE);
        assertEquals(text, "NO, this is the WELSH answer to your diabetes with insulin");
    }
}
