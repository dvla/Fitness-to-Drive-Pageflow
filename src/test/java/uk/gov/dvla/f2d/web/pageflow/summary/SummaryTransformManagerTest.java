package uk.gov.dvla.f2d.web.pageflow.summary;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.enums.Condition;
import uk.gov.dvla.f2d.model.enums.Language;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.cache.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.domain.PageFlowManager;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.DataTransformPipeline;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.SummaryLine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static uk.gov.dvla.f2d.model.constants.StringConstants.*;

public class SummaryTransformManagerTest extends TestCase
{
    private static final String DIABETES_CONDITION  = Condition.DIABETES.getName();

    private static final String DIABETES_QUESTION   = "diabetes-with-insulin";
    private static final String DIABETES_STEP       = "3";

    private static final String EYESIGHT_QUESTION   = "legal-eyesight-standard";
    private static final String EYESIGHT_STEP       = "10";

    public SummaryTransformManagerTest(String testName ) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(SummaryTransformManagerTest.class);
    }

    public void setUp() throws Exception {

    }

    public void tearDown() throws Exception {

    }

    private MedicalForm createForm(Service service, Language language) {
        PageFlowCacheManager cache = PageFlowCacheManager.getInstance();
        MedicalForm form = cache.createMedicalForm(service);
        form.getMessageHeader().setLanguage(language.getName());

        assertNotNull(form);

        form.getMessageHeader().getBreadcrumb().add(DIABETES_STEP);
        form.getMessageHeader().getBreadcrumb().add(EYESIGHT_STEP);
        assertEquals(form.getMessageHeader().getBreadcrumb().size(), 2);

        Map<String, MedicalCondition> conditions = cache.getConditions(Service.NOTIFY);

        MedicalCondition condition = conditions.get(DIABETES_CONDITION);
        assertNotNull(condition);

        form.setMedicalCondition(condition);

        MedicalQuestion diabetes = form.getMedicalCondition().getQuestions().get(DIABETES_QUESTION);
        diabetes.setAnswers(Arrays.asList(new String[]{YES}));

        MedicalQuestion eyesight = form.getMedicalCondition().getQuestions().get(EYESIGHT_QUESTION);
        eyesight.setAnswers(Arrays.asList(new String[]{NO}));

        return form;
    }

    /**
     * Test the summary for (Notify -> Diabetes -> Default).
     */
    public void testNotifyForDiabetesInDefaultLanguage() {
        MedicalForm form = createForm(Service.NOTIFY, Language.ENGLISH);

        DataTransformPipeline pipeline = DataTransformPipeline.create();
        assertNotNull(pipeline);

        List<SummaryLine> response = pipeline.transform(form);
        assertFalse(response.isEmpty());
        assertEquals(response.size(), 2);

        assertEquals(response.get(0).getLines().get(0), "you treat your diabetes with {b}insulin{/b}");
        assertEquals(response.get(1).getLines().get(0), "you {b}do not meet{/b} the legal eyesight standard for driving");
    }

    /**
     * Test the summary for (Notify -> Diabetes -> English).
     */
    public void testNotifyForDiabetesInEnglish() {
        MedicalForm form = createForm(Service.NOTIFY, Language.ENGLISH);

        DataTransformPipeline pipeline = DataTransformPipeline.create();
        assertNotNull(pipeline);

        List<SummaryLine> response = pipeline.transform(form);
        assertFalse(response.isEmpty());
        assertEquals(response.size(), 2);

        assertEquals(response.get(0).getLines().get(0), "you treat your diabetes with {b}insulin{/b}");
        assertEquals(response.get(1).getLines().get(0), "you {b}do not meet{/b} the legal eyesight standard for driving");
    }
}
