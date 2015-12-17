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
    private static final String DIABETES_CONDITION_ID   = "1";

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
        assertEquals(PageFlowCacheManager.getConditions().size(), 3);
    }

    /**
     * Test to see if all supported conditions are present
     */
    public void testAllSupportedMedicalConditions() {
        String[] supportedConditions = {
                "Diabetes","Glaucoma", "Epilepsy"
        };

        List<MedicalCondition> supported = PageFlowCacheManager.getConditions();

        assertEquals(supported.size(), supportedConditions.length);

        for(MedicalCondition condition : supported) {
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
        final String QUESTION_ID = "7";

        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        MedicalQuestion question = PageFlowCacheManager.getQuestionByConditionAndID(condition, QUESTION_ID);
        assertNotNull(question);

        assertEquals("hypoglycaemia-episode", question.getPage());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalConditionParametersPopulated() {
        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        assertEquals(new Long(DIABETES_CONDITION_ID), condition.getID());
        assertEquals("Diabetes", condition.getDisplayText());
        assertEquals("diabetes", condition.getValue());
        assertEquals("diab1 diabetes daibetes dibetes dyabetes diabbetes", condition.getSpellings());
        assertEquals("diabetes-help-page", condition.getInformationLink());
        assertEquals("Diabetes", condition.getConfiguration());

        assertNotNull(condition.toString());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalQuestionParametersPopulated() {
        final String QUESTION_ID = "7";

        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(new Long(QUESTION_ID), question.getID());
        assertEquals("hypoglycaemia-episode", question.getPage());
        assertEquals("Standard", question.getType());
        assertEquals(new Double(7), question.getIndex());
        assertEquals("Radio", question.getFormat());
        assertEquals("Yes", question.getValidate());
        assertEquals("No", question.getLogout());
        assertEquals("Y=9, N=8", question.getOptions());

        assertNotNull(question.getAnswers());

        System.out.println("ANSWERS: ["+question+")]");

        assertEquals(question.getAnswers().size(), 1);

        assertNotNull(question.toString());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testMedicalQuestionDefaultAnswersSupplied() {
        final String QUESTION_ID = "9";

        MedicalCondition condition = PageFlowCacheManager.getConditionByID(DIABETES_CONDITION_ID);
        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(new Long(QUESTION_ID), question.getID());
        assertEquals("insulin-declaration", question.getPage());
        assertEquals("Standard", question.getType());
        assertEquals(new Double(9), question.getIndex());
        assertEquals("Checkbox", question.getFormat());
        assertEquals("Yes", question.getValidate());
        assertEquals("No", question.getLogout());
        assertEquals("1=2, 2=3, 4=4, 8=5, 16=6, 21=7, 32=8", question.getOptions());

        assertNotNull(question.getAnswers());
        assertEquals(question.getAnswers().size(), 3);

        assertNotNull(question.toString());
    }
}
