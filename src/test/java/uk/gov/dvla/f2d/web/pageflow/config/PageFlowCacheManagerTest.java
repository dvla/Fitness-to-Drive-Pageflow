package uk.gov.dvla.f2d.web.pageflow.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.enums.Severity;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;
import uk.gov.dvla.f2d.web.pageflow.enums.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class PageFlowCacheManagerTest extends TestCase
{
    private static final String DIABETES_CONDITION_ID   = "diabetes";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PageFlowCacheManagerTest(String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( PageFlowCacheManagerTest.class );
    }

    /**
     * Test to see if all medical conditions have been loaded
     */
    public void testAllMedicalConditionsLoaded() {
        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        assertEquals(conditions.size(), 5);
    }

    /**
     * Test to see if all supported conditions are present
     */
    public void testAllSupportedMedicalConditions() {
        String[] supportedConditions = {
                "Diabetes", "Glaucoma", "Epilepsy", "Alcohol problems", "Drug misuse"
        };

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        assertEquals(conditions.size(), supportedConditions.length);

        for(MedicalCondition condition : conditions.values()) {
            String message = condition.getTitle() + " does not exist in the page flow.";
            assertTrue(message,  Arrays.asList(supportedConditions).contains(condition.getTitle()));
        }
    }

    /**
     * Test to see if only eligibility questions are returned for a condition
     */
    public void testUnverifiedQuestionsOnly() {
        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        List<MedicalQuestion> questions = PageFlowCacheManager.getUnverifiedPages(condition);

        assertNotNull(questions);
        assertTrue("No UNVERIFIED questions were found for condition", questions.size() > 0);

        boolean unverifiedQuestionsOnlyFound = true;
        for(MedicalQuestion question : questions) {
            if(!(question.getPage().equals(Page.UNVERIFIED.toString()))) {
                unverifiedQuestionsOnlyFound = false;
            }
        }

        assertTrue(unverifiedQuestionsOnlyFound);
    }


    /**
     * Test to see if only eligibility questions are returned for a condition
     */
    public void testVerifiedQuestionsOnly() {
        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        List<MedicalQuestion> questions = PageFlowCacheManager.getVerifiedPages(condition);

        assertNotNull(questions);
        assertTrue("No VERIFIED questions were found for condition", questions.size() > 0);

        boolean verifiedQuestionsOnlyFound = true;
        for(MedicalQuestion question : questions) {
            if(!(question.getPage().equals(Page.VERIFIED.toString()))) {
                verifiedQuestionsOnlyFound = false;
            }
        }

        assertTrue(verifiedQuestionsOnlyFound);
    }

    /**
     * Test to ascertain if we can find a medical condition by it's identifier.
     */
    public void testFindConditionByIdentifier() {
        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        assertEquals("Diabetes", condition.getTitle());
    }

    /**
     * Test to ascertain if we can find a particular question for a condition.
     */
    public void testFindConditionAndQuestionByIdentifiers() {
        final String QUESTION_ID = "hypoglycaemia-blood-sugar";

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        MedicalQuestion question = PageFlowCacheManager.getQuestionByConditionAndID(condition, QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalConditionParametersPopulated() {
        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        assertEquals("DIAB", condition.getID());
        assertEquals("Diabetes", condition.getTitle());
        assertEquals("diabetes", condition.getName());
        assertEquals(" ", condition.getType());
        assertEquals("DIAB1", condition.getForm());
        assertEquals(DIABETES_CONDITION_ID, condition.getSlug());
        assertEquals("diabetes-with-insulin", condition.getStart());
        assertEquals("diabetes", condition.getSlug());
        assertEquals("diabetes-and-driving", condition.getInformation());
        // Add synonyms check here.
        // Add casp check here.
        assertEquals(Severity.NOTIFIABLE, condition.getSeverity());
        assertEquals(Boolean.TRUE, condition.getDisplay());

        assertNotNull(condition.toString());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalQuestionParametersPopulated() {
        final String QUESTION_ID = "hypoglycaemia-blood-sugar";

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
        assertEquals("22", question.getStep());
        assertEquals(Page.VERIFIED.toString(), question.getPage());
        assertEquals(9, question.getOrder().intValue());
        assertEquals(Format.RADIO.toString(), question.getType());
        assertEquals(Boolean.TRUE, question.getValidate());
        assertEquals(Boolean.FALSE, question.getLogout());
        assertEquals("Y=7, N=9", question.getOptions());

        assertNotNull(question.getAnswers());
        assertEquals(question.getAnswers().size(), 0);

        assertNotNull(question.toString());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testMedicalQuestionDefaultAnswersSupplied() {
        final String QUESTION_ID = "insulin-declaration";

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
        assertEquals("9", question.getStep());
        assertEquals(Page.VERIFIED.toString(), question.getPage());
        assertEquals(12, question.getOrder().intValue());
        assertEquals(Format.RADIO.toString(), question.getType());
        assertEquals(Boolean.TRUE, question.getValidate());
        assertEquals(Boolean.FALSE, question.getLogout());
        assertEquals("Y=10, N=10", question.getOptions());

        assertNotNull(question.getAnswers());
        assertEquals(question.getAnswers().size(), 0);

        assertNotNull(question.toString());
    }
}
