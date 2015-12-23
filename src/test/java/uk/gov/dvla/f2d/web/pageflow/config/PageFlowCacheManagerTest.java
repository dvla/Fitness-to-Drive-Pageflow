package uk.gov.dvla.f2d.web.pageflow.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.web.pageflow.enums.Question;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

import java.util.*;

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
        assertEquals(PageFlowCacheManager.getSupportedConditions().size(), 2);
    }

    /**
     * Test to see if all supported conditions are present
     */
    public void testAllSupportedMedicalConditions() {
        String[] supportedConditions = {
                "Diabetes","Glaucoma"
        };

        Map<String, MedicalCondition> supported = PageFlowCacheManager.getSupportedConditions();
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
        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        List<MedicalQuestion> questions = PageFlowCacheManager.getEligibilityQuestions(condition);
        assertNotNull(questions);
        assertTrue("No eligibility questions were found for condition", questions.size() > 0);

        boolean eligibilityQuestionsOnlyFound = true;

        for(MedicalQuestion question : questions) {
            if(!(question.getType().equals(Question.ELIGIBILITY.toString()))) {
                eligibilityQuestionsOnlyFound = false;
            }
        }

        assertTrue(eligibilityQuestionsOnlyFound);
    }


    /**
     * Test to see if only eligibility questions are returned for a condition
     */
    public void testStandardQuestionsOnly() {
        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        List<MedicalQuestion> questions = PageFlowCacheManager.getStandardQuestions(condition);
        assertNotNull(questions);
        assertTrue("No standard questions were found for condition", questions.size() > 0);

        boolean standardQuestionsOnlyFound = true;

        for(MedicalQuestion question : questions) {
            if(!(question.getType().equals(Question.STANDARD.toString()))) {
                standardQuestionsOnlyFound = false;
            }
        }

        assertTrue(standardQuestionsOnlyFound);
    }

    /**
     * Test to ascertain if we can find a medical condition by it's identifier.
     */
    public void testFindConditionByIdentifier() {
        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        assertEquals("Diabetes", condition.getDisplayText());
    }

    /**
     * Test to ascertain if we can find a particular question for a condition.
     */
    public void testFindConditionAndQuestionByIdentifiers() {
        final String QUESTION_ID = "hypoglycaemia-blood-sugar";

        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        MedicalQuestion question = PageFlowCacheManager.getQuestionByConditionAndID(condition, QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalConditionParametersPopulated() {
        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        assertEquals(DIABETES_CONDITION_ID, condition.getID());
        assertEquals("Diabetes", condition.getDisplayText());
        assertEquals("diab1 diabetes daibetes dibetes dyabetes diabbetes", condition.getSpellings());
        assertEquals("diabetes-help-page", condition.getInformationLink());
        assertEquals("DIABETES_NOTIFY", condition.getConfiguration());

        assertNotNull(condition.toString());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalQuestionParametersPopulated() {
        final String QUESTION_ID = "hypoglycaemia-blood-sugar";

        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
        assertEquals("Standard", question.getType());
        assertEquals(8, question.getIndex().intValue());
        assertEquals("Radio", question.getFormat());
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

        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
        assertEquals("Standard", question.getType());
        assertEquals(11, question.getIndex().intValue());
        assertEquals("Checkbox", question.getFormat());
        assertEquals(Boolean.TRUE, question.getValidate());
        assertEquals(Boolean.FALSE, question.getLogout());
        assertEquals("1=2, 2=3, 4=4, 8=5, 16=6, 21=7, 32=8", question.getOptions());

        assertNotNull(question.getAnswers());
        assertEquals(question.getAnswers().size(), 0);

        assertNotNull(question.toString());
    }
}
