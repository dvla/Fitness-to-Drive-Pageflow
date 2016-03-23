package uk.gov.dvla.f2d.web.pageflow.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.enums.Page;

import java.util.*;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

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
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        assertEquals(form.getSupportedConditions().size(), 2);
    }

    /**
     * Test to see if all supported conditions are present
     */
    public void testAllSupportedMedicalConditions() {
        String[] supportedConditions = {
                "Diabetes","Glaucoma"
        };

        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        Map<String, MedicalCondition> supported = form.getSupportedConditions();

        assertEquals(supported.size(), supportedConditions.length);

        for(MedicalCondition condition : supported.values()) {
            String message = condition.getDisplayText()+" does not exist in the pageflow";
            assertTrue(message,  Arrays.asList(supportedConditions).contains(condition.getDisplayText()));
        }
    }

    /**
     * Test to see if only eligibility questions are returned for a condition
     */
    public void testEligibilityQuestionsOnly() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        List<MedicalQuestion> questions = PageFlowCacheManager.getEligibilityPages(condition);

        assertNotNull(questions);
        assertTrue("No eligibility questions were found for condition", questions.size() > 0);

        boolean eligibilityQuestionsOnlyFound = true;

        for(MedicalQuestion question : questions) {
            if(!(question.getPage().equals(Page.ELIGIBILITY.toString()))) {
                eligibilityQuestionsOnlyFound = false;
            }
        }

        assertTrue(eligibilityQuestionsOnlyFound);
    }


    /**
     * Test to see if only eligibility questions are returned for a condition
     */
    public void testStandardQuestionsOnly() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        List<MedicalQuestion> questions = PageFlowCacheManager.getQuestionPages(condition);

        assertNotNull(questions);
        assertTrue("No standard questions were found for condition", questions.size() > 0);

        boolean standardQuestionsOnlyFound = true;

        for(MedicalQuestion question : questions) {
            if(!(question.getPage().equals(Page.QUESTION.toString()))) {
                standardQuestionsOnlyFound = false;
            }
        }

        assertTrue(standardQuestionsOnlyFound);
    }

    /**
     * Test to ascertain if we can find a medical condition by it's identifier.
     */
    public void testFindConditionByIdentifier() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        assertEquals("Diabetes", condition.getDisplayText());
    }

    /**
     * Test to ascertain if we can find a particular question for a condition.
     */
    public void testFindConditionAndQuestionByIdentifiers() {
        final String QUESTION_ID = "hypoglycaemia-blood-sugar";

        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        MedicalQuestion question = PageFlowCacheManager.getQuestionByConditionAndID(condition, QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalConditionParametersPopulated() {
        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION_ID);

        assertNotNull(condition);

        assertEquals(DIABETES_CONDITION_ID, condition.getID());
        assertEquals("Diabetes", condition.getDisplayText());
        assertEquals("diab1 diabetes daibetes dibetes dyabetes diabbetes", condition.getSpellings());
        assertEquals("diabetes-help-page", condition.getInformationLink());
        assertEquals("DIABETES", condition.getConfiguration());

        assertNotNull(condition.toString());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalQuestionParametersPopulated() {
        final String QUESTION_ID = "hypoglycaemia-blood-sugar";

        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
        assertEquals("22", question.getStep());
        assertEquals("Question", question.getPage());
        assertEquals(8, question.getOrder().intValue());
        assertEquals("Radio", question.getType());
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

        MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);
        MedicalCondition condition = form.getSupportedConditions().get(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
        assertEquals("9", question.getStep());
        assertEquals("Question", question.getPage());
        assertEquals(11, question.getOrder().intValue());
        assertEquals("Radio", question.getType());
        assertEquals(Boolean.TRUE, question.getValidate());
        assertEquals(Boolean.FALSE, question.getLogout());
        assertEquals("Y=10, N=10", question.getOptions());

        assertNotNull(question.getAnswers());
        assertEquals(question.getAnswers().size(), 0);

        assertNotNull(question.toString());
    }
}
