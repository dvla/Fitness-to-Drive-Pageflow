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
    private static final String DIABETES_CONDITION  = "diabetes";

    private static final String DIABETES_QUESTION   = "diabetes-with-insulin";
    private static final String DIABETES_STEP       = "3";

    private static final String EYESIGHT_QUESTION   = "legal-eyesight-standard";
    private static final String EYESIGHT_STEP       = "10";

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

        form.getMessageHeader().getBreadcrumb().add(DIABETES_STEP);
        form.getMessageHeader().getBreadcrumb().add(EYESIGHT_STEP);
        assertEquals(form.getMessageHeader().getBreadcrumb().size(), 2);

        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        SummaryAggregator aggregator = SummaryAggregator.getInstance();
        assertNotNull(aggregator);

        MedicalQuestion diabetes = form.getMedicalCondition().getQuestions().get(DIABETES_QUESTION);
        diabetes.setAnswers(Arrays.asList(new String[]{YES}));

        MedicalQuestion eyesight = form.getMedicalCondition().getQuestions().get(EYESIGHT_QUESTION);
        eyesight.setAnswers(Arrays.asList(new String[]{NO}));

        List<String> response = aggregator.process(form);
        assertFalse(response.isEmpty());
        assertEquals(response.size(), 2);

        assertEquals(response.get(0), "You treat your diabetes with <b>insulin</b>");
        assertEquals(response.get(1), "You <b>do not meet</b> the legal eyesight standard for driving");
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

        form.getMessageHeader().getBreadcrumb().add(DIABETES_STEP);
        form.getMessageHeader().getBreadcrumb().add(EYESIGHT_STEP);
        assertEquals(form.getMessageHeader().getBreadcrumb().size(), 2);

        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        SummaryAggregator aggregator = SummaryAggregator.getInstance();
        assertNotNull(aggregator);

        MedicalQuestion diabetes = form.getMedicalCondition().getQuestions().get(DIABETES_QUESTION);
        diabetes.setAnswers(Arrays.asList(new String[]{YES}));

        MedicalQuestion eyesight = form.getMedicalCondition().getQuestions().get(EYESIGHT_QUESTION);
        eyesight.setAnswers(Arrays.asList(new String[]{NO}));

        List<String> response = aggregator.process(form);
        assertFalse(response.isEmpty());
        assertEquals(response.size(), 2);

        assertEquals(response.get(0), "You treat your diabetes with <b>insulin</b>");
        assertEquals(response.get(1), "You <b>do not meet</b> the legal eyesight standard for driving");
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

        form.getMessageHeader().getBreadcrumb().add(DIABETES_STEP);
        form.getMessageHeader().getBreadcrumb().add(EYESIGHT_STEP);
        assertEquals(form.getMessageHeader().getBreadcrumb().size(), 2);

        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        SummaryAggregator aggregator = SummaryAggregator.getInstance();
        assertNotNull(aggregator);

        MedicalQuestion diabetes = form.getMedicalCondition().getQuestions().get(DIABETES_QUESTION);
        diabetes.setAnswers(Arrays.asList(new String[]{NO}));

        MedicalQuestion eyesight = form.getMedicalCondition().getQuestions().get(EYESIGHT_QUESTION);
        eyesight.setAnswers(Arrays.asList(new String[]{YES}));

        List<String> response = aggregator.process(form);
        assertFalse(response.isEmpty());
        assertEquals(response.size(), 2);

        assertEquals(response.get(0), "");
        assertEquals(response.get(1), "You <b>meet</b> the legal eyesight standard for driving (Welsh)");
    }
}
