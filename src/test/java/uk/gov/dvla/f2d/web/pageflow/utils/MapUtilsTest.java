package uk.gov.dvla.f2d.web.pageflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.enums.Language;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.cache.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.helpers.ResourceHelper;

import java.io.IOException;
import java.util.Map;

public class MapUtilsTest extends TestCase
{
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public MapUtilsTest(String testName ) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(MapUtilsTest.class);
    }

    /**
     * Test to see if a medical questionnaire has been loaded from resources
     */
    public void testRetrieveSupportedMedicalConditionsForNotifyService() {
        PageFlowCacheManager cache = PageFlowCacheManager.getInstance();
        Map<String, MedicalCondition> conditions = cache.getConditions(Service.NOTIFY);

        assertTrue("No conditions were found in data structure", conditions.size() > 0);
        assertTrue("No questions were found in data structure", conditions.size() > 0);
    }

    /**
     * Test to see if our medical structure can be transformed into raw data.
     */
    public void testMapMedicalFormToDataString() {
        try {
            PageFlowCacheManager cache = PageFlowCacheManager.getInstance();
            MedicalForm form = cache.createMedicalForm(Service.NOTIFY);

            String data = PageFlowUtils.mapModelToString(form);

            assertNotNull(data);
            assertTrue(data.trim().length() > 0);

        } catch(JsonProcessingException ex) {
            fail("A JsonProcessingException should not have been raised!");
        }
    }

    /**
     * Test to see if our medical structure can be transformed into raw data.
     */
    public void testMapDataStringToMedicalForm() {
        try {
            String data = ResourceHelper.load("notify/config/", "medical-form.json");
            assertNotNull(data);

            MedicalForm form = PageFlowUtils.mapStringToModel(data);
            assertNotNull(form);

            assertEquals(form.getMessageHeader().getService(), Service.NOTIFY.getName());
            assertEquals(form.getMessageHeader().getLanguage(), Language.ENGLISH.getName());
            assertTrue(form.getMessageHeader().getBreadcrumb().isEmpty());
            assertTrue(form.getMessageHeader().getNotifications().isEmpty());

        } catch(JsonProcessingException ex) {
            fail("A JsonProcessingException should not have been raised!");

        } catch(IOException ex) {
            fail("A IOException should not have been raised!");
        }
    }
}
