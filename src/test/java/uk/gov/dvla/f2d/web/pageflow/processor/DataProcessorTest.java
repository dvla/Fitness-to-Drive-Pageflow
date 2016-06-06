package uk.gov.dvla.f2d.web.pageflow.processor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.processor.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataProcessorTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DataProcessorTest(String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( DataProcessorTest.class );
    }

    /**
     * Help method to return a basic, pre-populated radio group question.
     * @return Radio group question
     */
    private MedicalQuestion getRadioGroupQuestion() {
        final String DIABETES_CONDITION     = "diabetes";
        final String TARGET_QUESTION        = "hypoglycaemia-blood-sugar";

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);
        MedicalQuestion question = condition.getQuestions().get(TARGET_QUESTION);

        return question;
    }

    /**
     * Help method to return a basic, pre-populated checkbox group question.
     * @return Checkbox group question
     */
    private MedicalQuestion getCheckboxGroupQuestion() {
        final String DIABETES_CONDITION     = "diabetes";
        final String TARGET_QUESTION        = "car-bike-moped";

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);
        MedicalQuestion question = condition.getQuestions().get(TARGET_QUESTION);

        return question;
    }

    private MedicalQuestion getContinuePageQuestion() {
        final String DIABETES_CONDITION     = "diabetes";
        final String TARGET_QUESTION        = "hypoglycaemia-symptoms-info";

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);
        MedicalQuestion question = condition.getQuestions().get(TARGET_QUESTION);

        return question;
    }

    private MedicalQuestion getFormPageQuestion() {
        final String DIABETES_CONDITION     = "diabetes";
        final String TARGET_QUESTION        = "change-address";

        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(Service.NOTIFY.toString());
        MedicalCondition condition = conditions.get(DIABETES_CONDITION);
        MedicalQuestion question = condition.getQuestions().get(TARGET_QUESTION);

        return question;
    }

    /**
     * Test to determine if our factory retrieves the radio group processor for a question.
     */
    public void testRetrieveRadioGroupDataProcessor() {
        MedicalQuestion question = getRadioGroupQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        assertNotNull(processor);
        assertTrue("Processor is not the correct type.", (processor instanceof DataProcessorRadioGroupImpl));
    }

    /**
     * Test to determine if our factory retrieves the checkbox group processor for a question.
     */
    public void testRetrieveCheckboxGroupDataProcessor() {
        MedicalQuestion question = getCheckboxGroupQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        assertNotNull(processor);
        assertTrue("Processor is not the correct type.", (processor instanceof DataProcessorCheckboxGroupImpl));
    }

    /**
     * Test to determine if our factory retrieves the continue page processor for a question.
     */
    public void testRetrieveContinuePageDataProcessor() {
        MedicalQuestion question = getContinuePageQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        assertNotNull(processor);
        assertTrue("Processor is not the correct type.", (processor instanceof DataProcessorContinuePageImpl));
    }

    /**
     * Test to determine if our factory retrieves the continue page processor for a question.
     */
    public void testRetrieveFormPageDataProcessor() {
        MedicalQuestion question = getFormPageQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        assertNotNull(processor);
        assertTrue("Processor is not the correct type.", (processor instanceof DataProcessorFormPageImpl));
    }

    public void testRadioGroupDataProcessorDecisionSuccess() {
        final String ANSWER_FOR_QUESTION    = "Y";
        final String DECISION_FOR_QUESTION  = "7";

        MedicalQuestion question = getRadioGroupQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        // Check we have no decision for now.
        assertEquals(question.getDecision(), "");

        List<String> answers = new ArrayList<>();
        answers.add(ANSWER_FOR_QUESTION);

        question.setAnswers(answers);

        // Apply our data processor for this answer
        List<Notification> notifications = processor.validate();
        assertEquals(notifications.size(), 0);

        // Check a decision has now been made.
        Boolean expectedDecision = question.getDecision().equals(DECISION_FOR_QUESTION);
        assertTrue("Correct decision has not been made.", expectedDecision);
    }

    public void testRadioGroupDataProcessorDecisionFailure() {
        final String ANSWER_FOR_QUESTION    = "N";
        final String DECISION_FOR_QUESTION  = "9";

        MedicalQuestion question = getRadioGroupQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        // Check we have no decision for now.
        assertEquals(question.getDecision(), "");

        List<String> answers = new ArrayList<>();
        answers.add(ANSWER_FOR_QUESTION);

        question.setAnswers(answers);

        // Apply our data processor for this answer
        List<Notification> notifications = processor.validate();
        assertEquals(notifications.size(), 0);

        // Check a decision has now been made.
        Boolean expectedDecision = question.getDecision().equals(DECISION_FOR_QUESTION);
        assertTrue("Correct decision has not been made.", expectedDecision);
    }

    public void testCheckboxGroupDataProcessorDecision() {
        final String[] ANSWERS_FOR_QUESTIONS  = {"1","4","16"};
        final String DECISION_FOR_QUESTION  = "*";

        MedicalQuestion question = getCheckboxGroupQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        // Check we have no decision for now.m
        assertEquals(question.getDecision(), "");

        List<String> answers = Arrays.asList(ANSWERS_FOR_QUESTIONS);
        question.setAnswers(answers);

        // Apply our data processor for this answer
        List<Notification> notifications = processor.validate();
        assertEquals(notifications.size(), 0);

        // Check a decision has now been made.
        Boolean expectedDecision = question.getDecision().equals(DECISION_FOR_QUESTION);
        assertTrue("Correct decision has not been made.", expectedDecision);
    }

    public void testContinuePageDataProcessorDecision() {
        final String[] ANSWERS_FOR_QUESTIONS = {"Y","N","MAYBE"};
        final String DECISION_FOR_QUESTION  = "22";

        MedicalQuestion question = getContinuePageQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        // Check we have no decision for now.
        assertEquals(question.getDecision(), "");

        List<String> answers = Arrays.asList(ANSWERS_FOR_QUESTIONS);
        question.setAnswers(answers);

        // Apply our data processor for this answer
        List<Notification> notifications = processor.validate();
        assertEquals(notifications.size(), 0);

        // Check a decision has now been made.
        Boolean expectedDecision = question.getDecision().equals(DECISION_FOR_QUESTION);
        assertTrue("Correct decision has not been made.", expectedDecision);
    }

    public void testFormPageDataProcessorDecision() {
        final String[] ANSWERS_FOR_QUESTIONS = {"Y","N","MAYBE"};
        final String DECISION_FOR_QUESTION  = "A6.2";

        MedicalQuestion question = getFormPageQuestion();
        assertNotNull(question);

        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        // Check we have no decision for now.
        assertEquals(question.getDecision(), "");

        List<String> answers = Arrays.asList(ANSWERS_FOR_QUESTIONS);
        question.setAnswers(answers);

        // Apply our data processor for this answer
        List<Notification> notifications = processor.validate();
        assertEquals(notifications.size(), 0);

        // Check a decision has now been made.
        Boolean expectedDecision = question.getDecision().equals(DECISION_FOR_QUESTION);
        assertTrue("Correct decision has not been made.", expectedDecision);
    }
}
