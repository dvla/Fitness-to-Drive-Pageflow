package uk.gov.dvla.f2d.web.pageflow.domain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.cache.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.exceptions.PageNotFoundException;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.SummaryLine;
import uk.gov.dvla.f2d.web.pageflow.responses.PageResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageFlowManagerTest extends TestCase
{
    private static final String DIABETES_CONDITION      = "diabetes";

    private MedicalForm form;

    public PageFlowManagerTest(String testName ) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite( PageFlowManagerTest.class );
    }

    public void setUp() throws Exception {
        PageFlowCacheManager cache = PageFlowCacheManager.getInstance();
        Map<String, MedicalCondition> conditions = cache.getConditions(Service.NOTIFY);

        form = cache.createMedicalForm(Service.NOTIFY);
        form.setMedicalCondition(conditions.get(DIABETES_CONDITION));
    }

    public void tearDown() throws Exception {
        form = null;
    }

    public void testSupportedConditions() throws Exception {
        PageFlowManager manager = new PageFlowManager(form);
        Map<String, MedicalCondition> supported = manager.getSupportedConditions();

        assertTrue("No conditions found.", supported.size() > 0);
    }

    public void testFindQuestion() throws Exception {
        PageFlowManager manager = new PageFlowManager(form);
        MedicalQuestion question = manager.getQuestion("5");

        assertEquals("confirm-address", question.getID());
    }

    public void testSummaryTransformation() throws Exception {
        PageFlowManager manager = new PageFlowManager(form);
        List<SummaryLine> summary = manager.transform();

        assertNotNull(summary);
        assertEquals(0, summary.size());
    }

    public void testPrepareQuestion() throws Exception {
        PageFlowManager manager = new PageFlowManager(form);
        MedicalQuestion question = manager.prepareQuestion("diabetes-with-insulin");

        assertNotNull(question.getDecision());
        assertEquals(0, question.getAnswers().size());
        assertEquals(Boolean.TRUE, question.getValidate());
    }

    public void testRadioProcessQuestionSuccess() throws Exception {
        Map<String, String[]> entities = new HashMap<>();
        entities.put("answer", new String[] {"Y"});

        PageForm pageForm = new PageForm(entities);
        pageForm.setQuestion("diabetes-with-insulin");

        assertTrue(form.getMessageHeader().getNotifications().isEmpty());

        PageFlowManager manager = new PageFlowManager(form);
        PageResponse response = manager.processQuestion(pageForm);

        assertNotNull(response);
        assertEquals(0, form.getMessageHeader().getNotifications().size());

        assertFalse(response.isErrorsFound());
        assertFalse(response.isFlowFinished());

        assertEquals("expectations", response.getNextQuestion().getID());

        MedicalQuestion question = form.getMedicalCondition().getQuestions().get("diabetes-with-insulin");
        assertEquals("EXP1", question.getDecision());
    }

    public void testRadioProcessQuestionWithNoAnswer() throws Exception {
        Map<String, String[]> entities = new HashMap<>();
        entities.put("answer", new String[] {""});

        PageForm pageForm = new PageForm(entities);
        pageForm.setQuestion("diabetes-with-insulin");

        assertTrue(form.getMessageHeader().getNotifications().isEmpty());

        PageFlowManager manager = new PageFlowManager(form);
        PageResponse response = manager.processQuestion(pageForm);

        assertNotNull(response);
        assertEquals(1, form.getMessageHeader().getNotifications().size());

        assertTrue(response.isErrorsFound());
        assertFalse(response.isFlowFinished());

        assertEquals("diabetes-with-insulin", response.getNextQuestion().getID());
    }

    public void testRadioProcessQuestionWithInvalidAnswer() throws Exception {
        Map<String, String[]> entities = new HashMap<>();
        entities.put("answer", new String[] {"X"});

        PageForm pageForm = new PageForm(entities);
        pageForm.setQuestion("diabetes-with-insulin");

        assertTrue(form.getMessageHeader().getNotifications().isEmpty());

        PageFlowManager manager = new PageFlowManager(form);
        PageResponse response = manager.processQuestion(pageForm);

        assertNotNull(response);
        assertEquals(1, form.getMessageHeader().getNotifications().size());

        assertTrue(response.isErrorsFound());
        assertFalse(response.isFlowFinished());

        assertEquals("diabetes-with-insulin", response.getNextQuestion().getID());
    }

    public void testFindCurrentQuestion() throws Exception {
        String[] breadcrumbs = {"3", "EXP1", "5", "GEN1", "6a", "22", "7", "8", "8a", "9", "10"};

        form.getMessageHeader().setBreadcrumb(Arrays.asList(breadcrumbs));

        PageFlowManager manager = new PageFlowManager(form);
        MedicalQuestion current = manager.findCurrentQuestion();

        assertNotNull(current);
        assertEquals("10", current.getStep());
    }

    public void testFindCurrentQuestionWithSingleQuestion() throws Exception {
        String[] breadcrumbs = {"3"};

        form.getMessageHeader().setBreadcrumb(Arrays.asList(breadcrumbs));

        PageFlowManager manager = new PageFlowManager(form);
        MedicalQuestion current = manager.findCurrentQuestion();

        assertNotNull(current);
        assertEquals("3", current.getStep());
    }

    public void testFindCurrentQuestionWithEmptyBreadcrumb() throws Exception {
        String[] breadcrumbs = {};

        try {
            form.getMessageHeader().setBreadcrumb(Arrays.asList(breadcrumbs));

            PageFlowManager manager = new PageFlowManager(form);
            MedicalQuestion current = manager.findCurrentQuestion();

            fail("A PageNotFoundException should have been raised!");

        } catch(PageNotFoundException ex) {
            // Success
        }
    }

    public void testFindPreviousQuestion() throws Exception {
        String[] breadcrumbs = {"3", "EXP1", "5", "GEN1", "6a"};

        form.getMessageHeader().setBreadcrumb(Arrays.asList(breadcrumbs));

        PageFlowManager manager = new PageFlowManager(form);
        MedicalQuestion previous = manager.findPreviousQuestion();

        assertNotNull(previous);
        assertEquals("GEN1", previous.getStep());
    }

    public void testFindPreviousQuestionWithSingleQuestion() throws Exception {
        String[] breadcrumbs = {"3"};

        try {
            form.getMessageHeader().setBreadcrumb(Arrays.asList(breadcrumbs));

            PageFlowManager manager = new PageFlowManager(form);
            MedicalQuestion previous = manager.findPreviousQuestion();

            fail("A PageNotFoundException should have been raised!");

        } catch(PageNotFoundException ex) {
            // Success
        }
    }

    public void testFindPreviousQuestionWithTwoQuestions() throws Exception {
        String[] breadcrumbs = {"3", "EXP1"};

        form.getMessageHeader().setBreadcrumb(Arrays.asList(breadcrumbs));

        PageFlowManager manager = new PageFlowManager(form);
        MedicalQuestion previous = manager.findPreviousQuestion();

        assertNotNull(previous);
        assertEquals("3", previous.getStep());
    }
}
