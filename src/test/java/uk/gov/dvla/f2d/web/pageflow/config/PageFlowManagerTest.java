package uk.gov.dvla.f2d.web.pageflow.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.enums.Condition;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.enums.Severity;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;
import uk.gov.dvla.f2d.web.pageflow.enums.Page;

import java.util.Arrays;
import java.util.Map;

import static uk.gov.dvla.f2d.model.constants.StringConstants.EMPTY;

/**
 * Unit test for simple App.
 */
public class PageFlowManagerTest extends TestCase
{
    private static final String DIABETES_CONDITION = Condition.DIABETES.getName();

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PageFlowManagerTest(String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( PageFlowManagerTest.class );
    }

    /**
     * Test to see if all medical conditions have been loaded
     */
    public void testAllMedicalConditionsLoaded() {
        PageFlowManager manager = PageFlowManager.getInstance();
        Map<String, MedicalCondition> conditions = manager.getSupportedConditions(Service.NOTIFY);

        assertEquals(conditions.size(), 5);
    }

    /**
     * Test to see if all supported conditions are present
     */
    public void testAllSupportedMedicalConditions() {
        String[] supportedConditions = {
                "Diabetes", "Glaucoma", "Epilepsy", "Alcohol problems", "Drug misuse"
        };

        PageFlowManager manager = PageFlowManager.getInstance();
        Map<String, MedicalCondition> conditions = manager.getSupportedConditions(Service.NOTIFY);

        assertEquals(conditions.size(), supportedConditions.length);

        for(MedicalCondition condition : conditions.values()) {
            String message = condition.getTitle() + " does not exist in the page flow.";
            assertTrue(message,  Arrays.asList(supportedConditions).contains(condition.getTitle()));
        }
    }

    /**
     * Test to ascertain if we can find a medical condition by it's identifier.
     */
    public void testFindConditionByIdentifier() {
        PageFlowManager manager = PageFlowManager.getInstance();
        Map<String, MedicalCondition> conditions = manager.getSupportedConditions(Service.NOTIFY);
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);

        assertNotNull(condition);

        assertEquals("Diabetes", condition.getTitle());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalConditionParametersPopulated() {
        PageFlowManager manager = PageFlowManager.getInstance();
        Map<String, MedicalCondition> conditions = manager.getSupportedConditions(Service.NOTIFY);
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);

        assertNotNull(condition);

        assertEquals("DIAB", condition.getID());
        assertEquals("Diabetes", condition.getTitle());
        assertEquals("diabetes", condition.getName());
        assertEquals(EMPTY, condition.getType());
        assertEquals("DIAB1", condition.getForm());
        assertEquals(DIABETES_CONDITION, condition.getSlug());
        assertEquals("diabetes-with-insulin", condition.getStart());
        assertEquals("diabetes", condition.getSlug());
        assertEquals("diabetes-driving", condition.getInformation());
        assertTrue(condition.getSynonyms().isEmpty());
        assertEquals(1, condition.getCasp().size());
        assertTrue(condition.getCasp().contains("D01"));
        assertEquals(condition.getCeg(), "101");
        assertEquals(Severity.NOTIFIABLE, condition.getSeverity());
        assertEquals(Boolean.TRUE, condition.getDisplay());

        assertNotNull(condition.toString());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testAllMedicalQuestionParametersPopulated() {
        final String QUESTION_ID = "hypoglycaemia-blood-sugar";

        PageFlowManager cache = PageFlowManager.getInstance();
        Map<String, MedicalCondition> conditions = cache.getSupportedConditions(Service.NOTIFY);
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);

        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
        assertEquals("22", question.getStep());
        assertEquals(Page.VERIFIED.getName(), question.getPage());
        assertEquals(9, question.getOrder().intValue());
        assertEquals(Format.RADIO.getName(), question.getType());
        assertEquals(Boolean.TRUE, question.getValidate());
        assertEquals(Boolean.FALSE, question.getLogout());
        assertEquals("Y=7, N=9", question.getOptions());

        assertNotNull(question.getAnswers());
        assertEquals(question.getAnswers().size(), 0);

        assertNotNull(question.getConfiguration());
        assertTrue(question.getConfiguration().trim().length() > 0);

        assertNotNull(question.toString());
    }

    /**
     * Test to determine that all parameters are properly populated.
     */
    public void testMedicalQuestionDefaultAnswersSupplied() {
        final String QUESTION_ID = "insulin-declaration";

        PageFlowManager cache = PageFlowManager.getInstance();
        Map<String, MedicalCondition> conditions = cache.getSupportedConditions(Service.NOTIFY);
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);

        assertNotNull(condition);

        MedicalQuestion question = condition.getQuestions().get(QUESTION_ID);
        assertNotNull(question);

        assertEquals(QUESTION_ID, question.getID());
        assertEquals("9", question.getStep());
        assertEquals(Page.VERIFIED.getName(), question.getPage());
        assertEquals(12, question.getOrder().intValue());
        assertEquals(Format.RADIO.getName(), question.getType());
        assertEquals(Boolean.TRUE, question.getValidate());
        assertEquals(Boolean.FALSE, question.getLogout());
        assertEquals("Y=10, N=10", question.getOptions());

        assertNotNull(question.getAnswers());
        assertEquals(question.getAnswers().size(), 0);

        assertNotNull(question.toString());
    }
}
