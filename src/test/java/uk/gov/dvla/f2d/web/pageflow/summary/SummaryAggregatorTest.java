package uk.gov.dvla.f2d.web.pageflow.summary;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

import java.util.Arrays;
import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public class SummaryAggregatorTest extends TestCase
{
    private static final String ENGLISH_LANGUAGE    = "en";
    private static final String WELSH_LANGUAGE      = "cy";

    private static final String YES_ANSWER          = "Y";
    private static final String NO_ANSWER           = "N";

    private static final String DIABETES_CONDITION  = "diabetes";
    private static final String DIABETES_QUESTION   = "diabetes-with-insulin";
    private static final String EYESIGHT_QUESTION   = "legal-eyesight-standard";

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
     * Test the summary for (Notify -> Diabetes -> Default).
     */
    public void testNotifyForDiabetesInDefaultLanguage() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        assertNotNull(form);

        assertEquals(NOTIFY_SERVICE, form.getMessageHeader().getService());
        assertEquals(ENGLISH_LANGUAGE, form.getMessageHeader().getLanguage());

        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        SummaryAggregator aggregator = SummaryAggregator.getInstance();
        assertNotNull(aggregator);

        MedicalQuestion diabetes = form.getMedicalCondition().getQuestions().get(DIABETES_QUESTION);
        diabetes.setAnswers(Arrays.asList(new String[]{YES_ANSWER}));

        MedicalQuestion eyesight = form.getMedicalCondition().getQuestions().get(EYESIGHT_QUESTION);
        eyesight.setAnswers(Arrays.asList(new String[]{NO_ANSWER}));

        List<String> response = aggregator.process(form);
        assertFalse(response.isEmpty());
        assertTrue(response.size() == 2);

        assertEquals(response.get(0), "You treat your diabetes with insulin");
        assertEquals(response.get(1), "NO, your ENGLISH answer to [legal-eyesight-standard]");
    }

    /**
     * Test the summary for (Notify -> Diabetes -> English).
     */
    public void testNotifyForDiabetesInEnglish() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        assertNotNull(form);

        form.getMessageHeader().setLanguage(ENGLISH_LANGUAGE);

        assertEquals(NOTIFY_SERVICE, form.getMessageHeader().getService());
        assertEquals(ENGLISH_LANGUAGE, form.getMessageHeader().getLanguage());

        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        SummaryAggregator aggregator = SummaryAggregator.getInstance();
        assertNotNull(aggregator);

        MedicalQuestion diabetes = form.getMedicalCondition().getQuestions().get(DIABETES_QUESTION);
        diabetes.setAnswers(Arrays.asList(new String[]{YES_ANSWER}));

        MedicalQuestion eyesight = form.getMedicalCondition().getQuestions().get(EYESIGHT_QUESTION);
        eyesight.setAnswers(Arrays.asList(new String[]{NO_ANSWER}));

        List<String> response = aggregator.process(form);
        assertFalse(response.isEmpty());
        assertTrue(response.size() == 2);

        assertEquals(response.get(0), "You treat your diabetes with insulin");
        assertEquals(response.get(1), "NO, your ENGLISH answer to [legal-eyesight-standard]");
    }

    /**
     * Test the summary for (Notify -> Diabetes -> Welsh).
     */
    public void testNotifyForDiabetesInWelsh() {
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

        MedicalQuestion diabetes = form.getMedicalCondition().getQuestions().get(DIABETES_QUESTION);
        diabetes.setAnswers(Arrays.asList(new String[]{NO_ANSWER}));

        MedicalQuestion eyesight = form.getMedicalCondition().getQuestions().get(EYESIGHT_QUESTION);
        eyesight.setAnswers(Arrays.asList(new String[]{YES_ANSWER}));

        List<String> response = aggregator.process(form);
        assertFalse(response.isEmpty());
        assertTrue(response.size() == 2);

        assertEquals(response.get(0), "You do not treat your diabetes with insulin (Welsh)");
        assertEquals(response.get(1), "YES, your WELSH answer to [legal-eyesight-standard]");
    }
}
